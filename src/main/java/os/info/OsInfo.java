package os.info;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;
import org.xbill.DNS.ResolverConfig;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
//import oshi.software.os.OperatingSystem;
//import java.lang.management.OperatingSystemMXBean;
//import com.sun.management.OperatingSystemMXBean;

public class OsInfo {

    private static final com.sun.management.OperatingSystemMXBean sunOS = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    //private static final java.lang.management.OperatingSystemMXBean lanOS = ManagementFactory.getOperatingSystemMXBean();
    private static final SystemInfo si = new SystemInfo();
    private static final HardwareAbstractionLayer ha = si.getHardware();
    //private static final OperatingSystem os = si.getOperatingSystem();  
    private static String info;

    public static void main(String[] args) {
        /*System.out.println("\n_______OSHI:");
        System.out.println(ha.getNetworkIFs()[0].getDisplayName());
        System.out.println((ha.getNetworkIFs()[0].getBytesRecv() + 0.0) / (1024 * 1024));
        System.out.println((ha.getNetworkIFs()[0].getBytesSent() + 0.0) / (1024 * 1024));*/
        //
        info = "\n===============================\n";
        info = info + "Uptime, min = " + ha.getProcessor().getSystemUptime() / 60 + "\n";
        info = info + "\nPhysical Memory:\n-----------------\nTotal Physical Memory: " + sunOS.getTotalPhysicalMemorySize() / (1024 * 1024) + "\n";
        info = info + "Free Physical Memory: " + sunOS.getFreePhysicalMemorySize() / (1024 * 1024) + "\n";
        info = info + "Free Physical Memory OSHI: " + ha.getMemory().getAvailable() / (1024 * 1024) + "\n";
        /////
        if (!System.getProperties().getProperty("os.name").toLowerCase().contains("linux")) {
            info = info + "\nSwap:\n-----------------\nTotal swap: " + (sunOS.getTotalSwapSpaceSize() - sunOS.getTotalPhysicalMemorySize()) / (1024 * 1024) + "\n";
            info = info + "Free Swap: " + (sunOS.getFreeSwapSpaceSize() - sunOS.getTotalPhysicalMemorySize()) / (1024 * 1024) + "\n";
        } else {
            info = info + "\nSwap:\n-----------------\nTotal swap: " + (sunOS.getTotalSwapSpaceSize()) / (1024 * 1024) + "\n";
            info = info + "Free Swap: " + (sunOS.getFreeSwapSpaceSize()) / (1024 * 1024) + "\n";            
        }
        info = info + "Total Swap OSHI: " + ha.getMemory().getSwapTotal() / (1024 * 1024) + "\n";
        info = info + "Free Swap OSHI: " + (ha.getMemory().getSwapTotal() - ha.getMemory().getSwapUsed()) / (1024 * 1024) + "\n";
        ////
        info = info + "\nCPU:\n-----------------\nCPU kernels = " + Runtime.getRuntime().availableProcessors() + "\n";
        //info = info + "MBoard = "+ha.getComputerSystem().getModel() + "\n";
        info = info + "CPU = " + ha.getProcessor().getName() + "\n";
        // work in win 7
        info = info + "CPU SystemCpuLoad = " + sunOS.getSystemCpuLoad() + "\n";
        // not work in win
        info = info + "CPU SystemLoadAverage = " + sunOS.getSystemLoadAverage() + "\n";
        //info = info + "Parallelism = " + ForkJoinPool.getCommonPoolParallelism() + "\n";
        info = info + "\nOS:\n-----------------\nOS arch=" + System.getProperties().getProperty("os.arch") + "\n";
        info = info + "OS name=" + System.getProperties().getProperty("os.name") + "\n";
        info = info + "OS version=" + System.getProperties().getProperty("os.version") + "\n";
        info = info + "\nFS info\n-----------------\n";
        for (File root : File.listRoots()) {
            info = info + "File system root: " + root.getAbsolutePath() + "\n";
            info = info + "Total space Mb: " + root.getTotalSpace() / (1024 * 1024) + "\n";
            info = info + "Usable space Mb: " + root.getUsableSpace() / (1024 * 1024) + "\n";
        }
        info = info + "\nLocal network information:\n";
        Enumeration<NetworkInterface> enumerationNI = null;
        try {
            enumerationNI = NetworkInterface.getNetworkInterfaces();
            int j = 1;
            while (enumerationNI.hasMoreElements()) {
                NetworkInterface ni = enumerationNI.nextElement();
                if (ni.isUp()) {
                    info = info + "-----------\n" + j + ") Interface Name = " + ni.getName() + "\n";
                    info = info + "MTU = " + ni.getMTU() + "\n";
                    info = info + "State is UP = " + ni.isUp() + "\n";
                    //info = info  + "mac = " +  Base64.encodeBase64String(ni.getHardwareAddress()) + "\n";
                    Enumeration<InetAddress> niInetAddr = ni.getInetAddresses();
                    while (niInetAddr.hasMoreElements()) {
                        InetAddress ia = niInetAddr.nextElement();
                        info = info + "ip = " + ia.getHostAddress() + "\n";
                    }
                    j++;
                }
            }
        } catch (SocketException | NullPointerException ex) {
            //Logger.getLogger(CdiSysInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        int k = 1;
        for (NetworkIF nif : ha.getNetworkIFs()) {
            info = info + "==============\n" + k + ") Interface Name = " + nif.getName() + "\n";
            info = info + "Mb input = " + (nif.getBytesRecv() + 0.0) / (1024 * 1024) + "\n";
            info = info + "Mb output = " + (nif.getBytesSent() + 0.0) / (1024 * 1024) + "\n";
            info = info + "Mac = " + nif.getMacaddr() + "\n";
            info = info + "MTU = " + nif.getMTU() + "\n";
            info = info + "InpErrors = " + nif.getInErrors() + "\n";
            info = info + "InpPackets = " + nif.getPacketsRecv() + "\n";
            info = info + "OutErrors = " + nif.getOutErrors() + "\n";
            info = info + "OutPackets = " + nif.getPacketsSent() + "\n";
            info = info + "IPv4 = " + Arrays.toString(nif.getIPv4addr()) + "\n";
            info = info + "IPv6 = " + Arrays.toString(nif.getIPv6addr()) + "\n";
        }
        info = info + "\nSystem use DNS-servers:\n";
        Arrays.asList(ResolverConfig.getCurrentConfig().servers())
                .stream()
                .forEach(x -> info = info + x + "\n");
        info = info + "\n";
        
        System.out.println(info);
    }


}
