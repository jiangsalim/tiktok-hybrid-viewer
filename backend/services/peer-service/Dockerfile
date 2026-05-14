FROM golang:1.21-alpine AS builder
WORKDIR /app
COPY go.mod go.sum ./
RUN go mod download
COPY . .
RUN CGO_ENABLED=0 GOOS=linux go build -o peer-service main.go
FROM alpine:3.19
WORKDIR /app
COPY --from=builder /app/peer-service .
EXPOSE 8081
CMD ["./peer-service"]
