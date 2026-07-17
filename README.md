# NoRespLoadFix

A mod that fixes an issue where, when launching resource-intensive setups such as modpacks,
Windows detects that the system is unresponsive during loading and displays a dialog asking to terminate the process.



Please note that due to the nature of this mod, the “all” version includes JNA,
so the file size is slightly larger.

Since GhostingGuard.apply() is called at the very beginning of the FML load,
all basic high-load operations should now respond as intended.