package service

import (
"crypto/rand"
"encoding/hex"
"sync"
"time"
)

type PeerTask struct {
TaskID         string    `json:"task_id"`
RequesterID    string    `json:"requester_id"`
WorkerID       string    `json:"worker_id,omitempty"`
EncryptedQuery string    `json:"encrypted_query"`
Status         string    `json:"status"`
Result         string    `json:"result,omitempty"`
ErrorCode      string    `json:"error_code,omitempty"`
CreatedAt      time.Time `json:"created_at"`
ClaimedAt      time.Time `json:"claimed_at,omitempty"`
CompletedAt    time.Time `json:"completed_at,omitempty"`
TTLSeconds     int       `json:"ttl_seconds"`
}

type TaskRouter struct {
mu    sync.RWMutex
tasks map[string]*PeerTask
queue []string
registry *PeerRegistry
}

func NewTaskRouter(registry *PeerRegistry) *TaskRouter {
tr := &TaskRouter{
tasks:    make(map[string]*PeerTask),
registry: registry,
}
go tr.expiryLoop()
return tr
}

func (tr *TaskRouter) CreateTask(requesterID, encryptedQuery string, timeoutMs int) *PeerTask {
task := &PeerTask{
TaskID:         generateTaskID(),
RequesterID:    requesterID,
EncryptedQuery: encryptedQuery,
Status:         "pending",
CreatedAt:      time.Now(),
TTLSeconds:     30,
}
tr.mu.Lock()
tr.tasks[task.TaskID] = task
tr.queue = append(tr.queue, task.TaskID)
tr.mu.Unlock()
return task
}

func (tr *TaskRouter) PollTask(workerID string) *PeerTask {
tr.mu.Lock()
defer tr.mu.Unlock()
if len(tr.queue) == 0 {
return nil
}
taskID := tr.queue[0]
tr.queue = tr.queue[1:]
task := tr.tasks[taskID]
if task == nil || task.Status != "pending" {
return nil
}
task.WorkerID = workerID
task.Status = "claimed"
task.ClaimedAt = time.Now()
tr.registry.SetStatus(workerID, "busy")
return task
}

func (tr *TaskRouter) SubmitResult(taskID, workerID, status, result, errorCode string) bool {
tr.mu.Lock()
defer tr.mu.Unlock()
task := tr.tasks[taskID]
if task == nil || task.WorkerID != workerID {
return false
}
task.Status = status
task.Result = result
task.ErrorCode = errorCode
task.CompletedAt = time.Now()
tr.registry.SetStatus(workerID, "idle")
return true
}

func (tr *TaskRouter) GetTask(taskID string) *PeerTask {
tr.mu.RLock()
defer tr.mu.RUnlock()
return tr.tasks[taskID]
}

func (tr *TaskRouter) QueueLength() int {
tr.mu.RLock()
defer tr.mu.RUnlock()
return len(tr.queue)
}

func (tr *TaskRouter) expiryLoop() {
for {
time.Sleep(5 * time.Second)
tr.mu.Lock()
cutoff := time.Now().Add(-30 * time.Second)
var newQueue []string
for _, taskID := range tr.queue {
task := tr.tasks[taskID]
if task != nil && task.CreatedAt.After(cutoff) {
newQueue = append(newQueue, taskID)
} else if task != nil {
task.Status = "expired"
}
}
tr.queue = newQueue
tr.mu.Unlock()
}
}

func generateTaskID() string {
b := make([]byte, 8)
rand.Read(b)
return "task_" + hex.EncodeToString(b)
}
