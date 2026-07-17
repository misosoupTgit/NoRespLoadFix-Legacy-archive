# NoRespLoadFix

A mod that fixes an issue where, when running high-load configurations such as modpacks,
Windows detects that the system is unresponsive during loading and displays a dialog asking to terminate the process.



Since `GhostingGuard.apply()` is called at the very beginning of the FML load process,
all basic high-load operations should now respond as expected.