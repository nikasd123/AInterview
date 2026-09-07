## 2024-05-24 - Unsanitized Exception Messages Passed to UI
**Vulnerability:** Raw exception messages (`e.message` and `e.localizedMessage`) are caught in data/service classes and directly returned as error strings wrapped in state objects (`Resource.Error` and `SpeechState.Error`). This exposes internal stack traces, system paths, or API error details to the end-user UI.
**Learning:** Returning raw exception messages is an easy default when mapping exceptions to UI state, but it acts as an information leak. It existed because the `try-catch` blocks didn't explicitly sanitize the error response.
**Prevention:** Always return generic, user-friendly error messages from data services (e.g., 'An error occurred while generating questions') rather than passing the raw exception message strings upwards to the presentation layer.

## 2026-09-01 - Insecure Auto-Backup Enabled
**Vulnerability:** The application was configured with `android:allowBackup="true"` in the AndroidManifest.xml. This allows an attacker with physical access or an ADB connection to extract sensitive data from the app's internal storage via `adb backup`.
**Learning:** Default configurations in Android often prioritize convenience over security. Enabling auto-backup by default without explicitly configuring what is backed up can lead to data leakage.
**Prevention:** Always explicitly set `android:allowBackup="false"` unless automated backups are specifically required and explicitly configured to exclude sensitive data via `android:fullBackupContent` and `android:dataExtractionRules`.
## 2026-10-25 - Information Exposure Through Logcat and Error Streams
**Vulnerability:** Raw exceptions were being logged to the console using `e.printStackTrace()` or directly passed to `android.util.Log.e("tag", "msg", e)` in data services, as well as being wrapped into `IllegalStateException` throwing unhandled exceptions. This exposes internal stack traces, system paths, and API error details to Logcat.
**Learning:** While logging stack traces can be helpful for debugging, it is a bad practice for security in production as it leaks internal application structure and potentially sensitive snippets.
**Prevention:** Always log sanitized error messages and handle errors securely without exposing the raw `Exception` object, especially in cases where `allowBackup` is no longer the only concern. Use generic messages for user feedback or logs.
