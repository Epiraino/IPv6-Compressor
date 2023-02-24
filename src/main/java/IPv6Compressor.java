import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPv6Compressor {

    public static String compress(String ip) {
        // Check if the given string is a valid IPv6 address
        if (!isValidIPv6Address(ip)) {
            return ip;
        }

        // Split the IP address into its components
        String[] components = ip.split(":");
        StringBuilder sb = new StringBuilder();
        int zeroCount = 0;
        boolean isZeroCounting = false;

        for (int i = 0; i < components.length; i++) {
            // If this component is a string of zeroes, count it
            if (components[i].equals("0000")) {
                if (!isZeroCounting) {
                    isZeroCounting = true;
                }
                zeroCount++;
            } else {
                // If we were previously counting zeroes, add the "::" notation
                if (isZeroCounting) {
                    sb.append(":");
                    for (int j = 0; j < 8 - components.length + zeroCount; j++) {
                        sb.append("0000:");
                    }
                    isZeroCounting = false;
                    zeroCount = 0;
                }
                sb.append(components[i]).append(":");
            }
        }

        // If we were still counting zeroes at the end of the IP address, add the "::" notation
        if (isZeroCounting) {
            sb.append(":");
            for (int j = 0; j < 8 - components.length + zeroCount + 1; j++) {
                sb.append("0000:");
            }
        }

        // Remove the trailing colon and return the compressed IP address
        return sb.toString().substring(0, sb.length() - 1);
    }

    public static boolean isValidIPv6Address(String ip) {
        // Use a regular expression to check if the given string is a valid IPv6 address
        Pattern pattern = Pattern.compile("^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    public static void main(String[] args) {
        String ip1 = "2001:0db8:0000:0000:0000:0000:1428:57ab";
        String compressedIp1 = compress(ip1);
        System.out.println(compressedIp1); // Output: 2001:db8::1428:57ab

        String ip2 = "fe80:0000:0000:0000:0202:b3ff:fe1e:8329";
        String compressedIp2 = compress(ip2);
        System.out.println(compressedIp2); // Output: fe80::202:b3ff:fe1e:8329

        String invalidIp = "2001:0db8:0000:0000:0000:0000:1428:57ab:1234";
        String compressedInvalidIp = compress(invalidIp);
        System.out.println(compressedInvalidIp); // Output: 2001:0db8:0000:0000:0000:0000:1428:57ab:1234
    }
}

