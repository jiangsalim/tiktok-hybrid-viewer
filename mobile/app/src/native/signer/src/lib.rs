mod utils;
mod mobile_signer;
mod web_signer;

use std::ffi::{CStr, CString};
use std::os::raw::c_char;
use mobile_signer::MobileSigner;
use web_signer::WebSigner;

#[no_mangle]
pub extern "C" fn rust_sign_mobile_api(
    url_ptr: *const c_char,
    device_id_ptr: *const c_char,
    timestamp: u64,
) -> *mut c_char {
    let url = unsafe { CStr::from_ptr(url_ptr) }.to_str().unwrap_or("");
    let device_id = unsafe { CStr::from_ptr(device_id_ptr) }.to_str().unwrap_or("");

    let signed = MobileSigner::sign(url, device_id, timestamp);

    let result = format!(
        "{}|{}|{}|{}",
        signed.x_argus, signed.x_gorgon, signed.x_ladon, signed.x_khronos
    );

    CString::new(result).unwrap().into_raw()
}

#[no_mangle]
pub extern "C" fn rust_sign_web_api(
    url_ptr: *const c_char,
    user_agent_ptr: *const c_char,
    timestamp: u64,
) -> *mut c_char {
    let url = unsafe { CStr::from_ptr(url_ptr) }.to_str().unwrap_or("");
    let user_agent = unsafe { CStr::from_ptr(user_agent_ptr) }.to_str().unwrap_or("");

    let signed = WebSigner::sign(url, user_agent, timestamp);

    let result = format!(
        "{}|{}|{}",
        signed.x_bogus, signed.x_gnarly, signed.timestamp
    );

    CString::new(result).unwrap().into_raw()
}

#[no_mangle]
pub extern "C" fn rust_free_string(ptr: *mut c_char) {
    if !ptr.is_null() {
        unsafe {
            let _ = CString::from_raw(ptr);
        }
    }
}
