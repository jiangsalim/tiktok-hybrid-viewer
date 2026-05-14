use crate::utils;

pub struct WebSigner;

impl WebSigner {
    pub fn sign(url: &str, user_agent: &str, timestamp: u64) -> WebSignedResult {
        let x_bogus = Self::generate_x_bogus(url, user_agent, timestamp);
        let x_gnarly = Self::generate_x_gnarly(url, timestamp);

        WebSignedResult {
            x_bogus,
            x_gnarly,
            timestamp,
        }
    }

    fn generate_x_bogus(url: &str, user_agent: &str, timestamp: u64) -> String {
        let query = if let Some(pos) = url.find('?') {
            &url[pos + 1..]
        } else {
            ""
        };

        let raw = format!("{}{}{}", query, user_agent, timestamp);
        let hash = utils::md5_hash(&raw);

        let first = &hash[..8];
        let second = utils::base64_encode(&hash[8..16].as_bytes());
        let third = utils::hex_encode(&hash[16..24].as_bytes());
        let fourth = utils::base64_encode(&hash[24..].as_bytes());

        format!("{}{}{}{}", first, second, third, fourth)
    }

    fn generate_x_gnarly(url: &str, timestamp: u64) -> String {
        let raw = format!("{}{}", url, timestamp);
        let hash = utils::sha256_hash(&raw);
        utils::base64_encode(hash.as_bytes())
    }
}

pub struct WebSignedResult {
    pub x_bogus: String,
    pub x_gnarly: String,
    pub timestamp: u64,
}
