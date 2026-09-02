## 2024-05-24 - Unsanitized Exception Messages Passed to UI
**Vulnerability:** Raw exception messages (`e.message` and `e.localizedMessage`) are caught in data/service classes and directly returned as error strings wrapped in state objects (`Resource.Error` and `SpeechState.Error`). This exposes internal stack traces, system paths, or API error details to the end-user UI.
**Learning:** Returning raw exception messages is an easy default when mapping exceptions to UI state, but it acts as an information leak. It existed because the `try-catch` blocks didn't explicitly sanitize the error response.
**Prevention:** Always return generic, user-friendly error messages from data services (e.g., 'An error occurred while generating questions') rather than passing the raw exception message strings upwards to the presentation layer.

## 2024-05-24 - Insecure Data Backup Allowed
**Vulnerability:** `android:allowBackup="true"` was set in `AndroidManifest.xml`, allowing anyone with physical access and ADB (or cloud backup) to extract the app's entire data directory, including the Room database with sensitive interview session recordings.
**Learning:** Default Android configurations often enable `allowBackup`, which acts as an attack vector for local data extraction, especially for apps storing PII or private user data locally.
**Prevention:** Set `android:allowBackup="false"` in the manifest to block adb backups. For apps that require cloud backup, selectively exclude sensitive files (e.g., databases and shared preferences containing tokens) using `data_extraction_rules.xml` and `backup_rules.xml`.
