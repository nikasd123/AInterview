## 2024-05-24 - Unsanitized Exception Messages Passed to UI
**Vulnerability:** Raw exception messages (`e.message` and `e.localizedMessage`) are caught in data/service classes and directly returned as error strings wrapped in state objects (`Resource.Error` and `SpeechState.Error`). This exposes internal stack traces, system paths, or API error details to the end-user UI.
**Learning:** Returning raw exception messages is an easy default when mapping exceptions to UI state, but it acts as an information leak. It existed because the `try-catch` blocks didn't explicitly sanitize the error response.
**Prevention:** Always return generic, user-friendly error messages from data services (e.g., 'An error occurred while generating questions') rather than passing the raw exception message strings upwards to the presentation layer.

## 2026-09-01 - Insecure Auto-Backup Enabled
**Vulnerability:** The application was configured with `android:allowBackup="true"` in the AndroidManifest.xml. This allows an attacker with physical access or an ADB connection to extract sensitive data from the app's internal storage via `adb backup`.
**Learning:** Default configurations in Android often prioritize convenience over security. Enabling auto-backup by default without explicitly configuring what is backed up can lead to data leakage.
**Prevention:** Always explicitly set `android:allowBackup="false"` unless automated backups are specifically required and explicitly configured to exclude sensitive data via `android:fullBackupContent` and `android:dataExtractionRules`.

## 2026-10-27 - Missing Input Length Limits and Stack Trace Leaks
**Vulnerability:** User answers passed to the LLM (Gemini API) in `AiInterviewerServiceImpl` were not length-limited, making the system susceptible to Token Exhaustion (DoS) and increasing the Prompt Injection surface. Additionally, raw exception objects were passed to Android `Log.e()` and `e.printStackTrace()`, exposing internal stack traces in system logs.
**Learning:** Hardcoded text accumulation in Speech-to-Text flows can grow infinitely. LLM endpoints need strict input bounds. Android's `Log.e` prints the full stack trace when passed an exception, which shouldn't be used for generic parsing failures in production.
**Prevention:** Always use `.take(max_length)` or similar length validation on arbitrary user strings sent to APIs. Use `Log.e(tag, message)` without the exception object for handled errors to prevent stack trace leakage.
