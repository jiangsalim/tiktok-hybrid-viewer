use md5::{Md5, Digest};
use sha2::Sha256;
use base64::{Engine as _, engine::general_purpose};
use hex;

pub fn md5_hash(input: &str) -> String {
    let mut hasher = Md5::new();
    hasher.update(input.as_bytes());
    format!("{:x}", hasher.finalize())
}

pub fn sha256_hash(input: &str) -> String {
    let mut hasher = Sha256::new();
    hasher.update(input.as_bytes());
    format!("{:x}", hasher.finalize())
}

pub fn base64_encode(input: &[u8]) -> String {
    general_purpose::STANDARD.encode(input)
}

pub fn base64_decode(input: &str) -> Option<Vec<u8>> {
    general_purpose::STANDARD.decode(input).ok()
}

pub fn hex_encode(input: &[u8]) -> String {
    hex::encode(input)
}

pub fn hex_decode(input: &str) -> Option<Vec<u8>> {
    hex::decode(input).ok()
}

pub fn generate_random_hex(length: usize) -> String {
    use rand::Rng;
    let mut rng = rand::thread_rng();
    let bytes: Vec<u8> = (0..length).map(|_| rng.gen()).collect();
    hex_encode(&bytes)
}
