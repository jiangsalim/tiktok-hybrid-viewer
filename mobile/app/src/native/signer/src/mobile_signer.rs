use crate::utils;

pub struct MobileSigner;

impl MobileSigner {
    pub fn sign(url: &str, device_id: &str, timestamp: u64) -> SignedHeaders {
        let url_path = Self::extract_path(url);
        let query_string = Self::extract_query(url);

        let x_argus = Self::generate_x_argus(device_id, timestamp, &url_path);
        let x_gorgon = Self::generate_x_gorgon(&url_path, &query_string, timestamp);
        let x_ladon = Self::generate_x_ladon(device_id);
        let x_khronos = timestamp.to_string();

        SignedHeaders {
            x_argus,
            x_gorgon,
            x_ladon,
            x_khronos,
        }
    }

    fn extract_path(url: &str) -> String {
        if let Some(pos) = url.find("tiktokv.com") {
            let after_domain = &url[pos + 10..];
            if let Some(q_pos) = after_domain.find('?') {
                after_domain[..q_pos].to_string()
            } else {
                after_domain.to_string()
            }
        } else {
            url.to_string()
        }
    }

    fn extract_query(url: &str) -> String {
        if let Some(pos) = url.find('?') {
            url[pos + 1..].to_string()
        } else {
            String::new()
        }
    }

    fn generate_x_argus(device_id: &str, timestamp: u64, path: &str) -> String {
        let raw = format!("{}{}{}", device_id, timestamp, path);
        let hash = utils::md5_hash(&raw);
        let prefix = "MS4wLjABAAAA";
        let encoded = utils::base64_encode(hash.as_bytes());
        format!("{}{}", prefix, encoded)
    }

    fn generate_x_gorgon(path: &str, query: &str, timestamp: u64) -> String {
        let raw = format!("{}{}{}8404", path, query, timestamp);
        let hash = utils::md5_hash(&raw);
        let prefix = "8404";
        let suffix = utils::generate_random_hex(16);
        format!("{}{}{}", prefix, hash, suffix)
    }

    fn generate_x_ladon(device_id: &str) -> String {
        let raw = format!("{}:{}", device_id, utils::generate_random_hex(8));
        utils::base64_encode(raw.as_bytes())
    }
}

pub struct SignedHeaders {
    pub x_argus: String,
    pub x_gorgon: String,
    pub x_ladon: String,
    pub x_khronos: String,
}
