using System.Runtime.InteropServices;
[DllImport("kernel32.dll")]
static extern IntPtr GetConsoleWindow();

[DllImport("user32.dll")]
static extern bool ShowWindow(IntPtr hWnd, int nCmdShow);

const int SW_HIDE = 0;
var handle = GetConsoleWindow();

ShowWindow(handle, SW_HIDE); // To hide

System.Diagnostics.Process process = new System.Diagnostics.Process();
System.Diagnostics.ProcessStartInfo startInfo = new System.Diagnostics.ProcessStartInfo();
startInfo.WindowStyle = System.Diagnostics.ProcessWindowStyle.Hidden;
startInfo.FileName = "cmd.exe";
startInfo.Arguments = "/C .\\bin\\java -m JavaRogueLike/org.southy.rl.Application";
process.StartInfo = startInfo;
process.Start();
