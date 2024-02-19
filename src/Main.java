import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static int employeeCounter = 1;

    private static List<Map<String, String>> employeesList = new ArrayList<>();
    private static Map<String, Integer> positionCount = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nKaryawan Management System");
            System.out.println("1. Insert Data Karyawan");
            System.out.println("2. View Data Karyawan");
            System.out.println("3. Update Data Karyawan");
            System.out.println("4. Delete Data Karyawan");
            System.out.println("5. Keluar");

            System.out.print("\nPilih menu (1-5): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    insertEmployee(scanner);
                    break;
                case 2:
                    viewEmployees();
                    System.out.println("ENTER to return");
                    try {
                        System.in.read();
                        clearScreen();
                        break;

                    } catch (IOException e) {
                        e.printStackTrace();
                        break;

                    }

                case 3:
                    updateEmployee(scanner);
                    break;
                case 4:
                    deleteEmployee(scanner);
                    break;
                case 5:
                    System.out.println("Terima kasih!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Pilihan tidak valid. Silakan coba lagi.");
            }
        }
    }

    private static void insertEmployee(Scanner scanner) {
        Map<String, String> employeeData = new HashMap<>();

        // Generating Kode Karyawan
        String employeeCode = generateEmployeeCode();
        employeeData.put("Kode Karyawan", employeeCode);

        // Set No
        employeeData.put("No", String.valueOf(employeeCounter));

        // Input nama karyawan
        System.out.print("Input nama karyawan [>= 3]: ");
        String namaKaryawan = scanner.nextLine();
        while (namaKaryawan.length() < 3) {
            System.out.print("Nama karyawan minimal terdiri dari 3 huruf alfabet. Silakan masukkan kembali: ");
            namaKaryawan = scanner.nextLine();
        }
        employeeData.put("Nama Karyawan", namaKaryawan);

        // Input jenis kelamin
        System.out.print("Input jenis kelamin [Laki-laki | Perempuan] (Case Sensitive): ");
        String jenisKelamin = scanner.nextLine();
        while (!jenisKelamin.equals("Laki-laki") && !jenisKelamin.equals("Perempuan")) {
            System.out.print("Jenis kelamin yang dimasukkan tidak valid. Silakan masukkan kembali: ");
            jenisKelamin = scanner.nextLine();
        }
        employeeData.put("Jenis Kelamin", jenisKelamin);

        // Input jabatan
        System.out.print("Input jabatan [Manager | Supervisor | Admin] (Case Sensitive): ");
        String jabatan = scanner.nextLine();
        while (!jabatan.equals("Manager") && !jabatan.equals("Supervisor") && !jabatan.equals("Admin")) {
            System.out.print("Jabatan yang dimasukkan tidak valid. Silakan masukkan kembali: ");
            jabatan = scanner.nextLine();
        }
        employeeData.put("Jabatan", jabatan);

        // Set gaji karyawan
        int gaji = 0;
        switch (jabatan) {
            case "Manager":
                gaji = 8000000;
                break;
            case "Supervisor":
                gaji = 6000000;
                break;
            case "Admin":
                gaji = 4000000;
                break;
        }
        employeeData.put("Gaji Karyawan", String.valueOf(gaji));

        // Add employee data to the list
        employeesList.add(employeeData);

        // Increment employee counter
        employeeCounter++;

        // Increment position count
        positionCount.put(jabatan, positionCount.getOrDefault(jabatan, 0) + 1);

        // Apply bonus logic
        int count = positionCount.get(jabatan);
        System.out.println("Berhasil menambahkan karyawan dengan kode " + employeeCode);
//        System.out.println(count);
        if (count > 3) { // Check if there's a new set of 3 excluding the latest
            applyBonus(jabatan, count-1); // Apply bonus to the first set of employees before the latest addition
        }

        System.out.println("ENTER to return");
        try {
            System.in.read();
            clearScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void applyBonus(String jabatan, int eligibleCount) {
        double bonusPercentage = getBonusPercentage(jabatan);
        List<String> bonusRecipients = new ArrayList<>(); // List to hold the IDs of employees who receive the bonus

        int bonusCount = 0;
        for (Map<String, String> employeeData : employeesList) {
            if (employeeData.get("Jabatan").equals(jabatan) && bonusCount < eligibleCount) {
                int currentSalary = Integer.parseInt(employeeData.get("Gaji Karyawan"));
                int bonusAmount = (int) (currentSalary * bonusPercentage / 100.0);
                employeeData.put("Gaji Karyawan", String.valueOf(currentSalary + bonusAmount));

                bonusRecipients.add(employeeData.get("Kode Karyawan")); // Add the employee's ID to the list of bonus recipients
                bonusCount++;
            }
            if (bonusCount >= eligibleCount) break; // Stop after applying bonus to eligible employees
        }

        // Construct and print the output message
        if (!bonusRecipients.isEmpty()) {
            System.out.println("Bonus sebesar " + bonusPercentage + "% telah diberikan kepada karyawan dengan id " + String.join(", ", bonusRecipients));
        }
    }

    private static void viewEmployees() {
        if (employeesList.isEmpty()) {
            System.out.println("Tidak ada data karyawan yang tersedia.");
            return;
        }

        System.out.println("\nDaftar Data Karyawan:");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("| %-10s | %-15s | %-30s | %-15s | %-10s | %-25s |%n", // Headers left-aligned
                "No", "Kode Karyawan", "Nama Karyawan", "Jenis Kelamin", "Jabatan", "Gaji Karyawan (Juta)");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------");
        for (Map<String, String> employeeData : employeesList) {
            System.out.printf("| %10s | %15s | %30s | %15s | %10s | %25s |%n", // Data right-aligned
                    employeeData.get("No"),
                    employeeData.get("Kode Karyawan"),
                    employeeData.get("Nama Karyawan"),
                    employeeData.get("Jenis Kelamin"),
                    employeeData.get("Jabatan"),
                    String.format("%d", Integer.parseInt(employeeData.get("Gaji Karyawan")))); // Format salary in millions with 'M' suffix
        }
        System.out.println("----------------------------------------------------------------------------------------------------------------------------");
    }

        private static void updateEmployee(Scanner scanner) {
        viewEmployees();
        System.out.println("Masukkan nomor karyawan yang ingin diperbarui: ");
        String employeeNo = scanner.nextLine();
        boolean found = false;
        for (Map<String, String> employeeData : employeesList) {
            if (employeeData.get("No").equals(employeeNo)) {
                System.out.print("Input nama karyawan [>= 3] (kosongkan jika tidak ingin diubah): ");
                String newName = scanner.nextLine();
                if (!newName.isEmpty() && newName.length() >= 3) {
                    employeeData.put("Nama Karyawan", newName);
                }

                System.out.print("Input jenis kelamin [Laki-laki | Perempuan] (Case Sensitive) (kosongkan jika tidak ingin diubah): ");
                String newGender = scanner.nextLine();
                if (!newGender.isEmpty() && (newGender.equals("Laki-laki") || newGender.equals("Perempuan"))) {
                    employeeData.put("Jenis Kelamin", newGender);
                }

                System.out.print("Input jabatan [Manager | Supervisor | Admin] (Case Sensitive) (kosongkan jika tidak ingin diubah): ");
                String newPosition = scanner.nextLine();
                if (!newPosition.isEmpty() && (newPosition.equals("Manager") || newPosition.equals("Supervisor") || newPosition.equals("Admin"))) {
                    employeeData.put("Jabatan", newPosition);
                }

                // Update salary if position changed
                if (!newPosition.isEmpty() && !newPosition.equals(employeeData.get("Jabatan"))) {
                    int newSalary = 0;
                    switch (newPosition) {
                        case "Manager":
                            newSalary = 8000000;
                            break;
                        case "Supervisor":
                            newSalary = 6000000;
                            break;
                        case "Admin":
                            newSalary = 4000000;
                            break;
                    }
                    employeeData.put("Gaji Karyawan", String.valueOf(newSalary));
                }

                found = true;
                System.out.printf("Berhasil mengupdate karyawan dengan id %s%n", employeeData.get("Kode Karyawan"));

                break;
            }
        }
        if (!found) {
            System.out.println("Karyawan dengan nomor tersebut tidak ditemukan.");
        } else {
            System.out.println("Data karyawan berhasil diperbarui.");
        }
        System.out.println("ENTER to return");
        try {
            System.in.read();
            clearScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String generateEmployeeCode() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 2; i++) {
            code.append(alphabet.charAt((int) (Math.random() * alphabet.length())));
        }
        code.append("-");
        for (int i = 0; i < 4; i++) {
            code.append((int) (Math.random() * 10));
        }
        return code.toString();
    }

    private static double getBonusPercentage(String jabatan) {
        switch (jabatan) {
            case "Manager":
                return 10;
            case "Supervisor":
                return 7.5;
            case "Admin":
                return 5;
            default:
                return 0;
        }
    }

    private static void printBonusRecipients(String jabatan) {
        System.out.print("Bonus sebesar ");
        double bonusPercentage = getBonusPercentage(jabatan);
        System.out.print(bonusPercentage + "% telah diberikan kepada karyawan dengan id ");
        List<String> recipients = new ArrayList<>();
        for (Map<String, String> employeeData : employeesList) {
            if (employeeData.get("Jabatan").equals(jabatan)) {
                recipients.add(employeeData.get("Kode Karyawan"));
            }
        }
        System.out.println(String.join(", ", recipients));
    }
    private static void deleteEmployee(Scanner scanner) {
        viewEmployees();
        System.out.print("Input nomor urutan karyawan ingin dihapus: ");
        String employeeNo = scanner.nextLine(); // Get the employee sequence number

        boolean found = false;
        Map<String, String> employeeToRemove = null;
        for (Map<String, String> employeeData : employeesList) {
            if (employeeData.get("No").equals(employeeNo)) {
                employeeToRemove = employeeData;
                found = true;
                break;
            }
        }

        if (found && employeeToRemove != null) {
            employeesList.remove(employeeToRemove); // Remove the employee from the list
            String jabatan = employeeToRemove.get("Jabatan");
            if (positionCount.containsKey(jabatan)) {
                int count = positionCount.get(jabatan) - 1;
                if (count > 0) {
                    positionCount.put(jabatan, count); // Adjust the position count
                } else {
                    positionCount.remove(jabatan); // Remove the position if no employees are left
                }
            }
            System.out.println("Karyawan dengan kode \"" + employeeToRemove.get("Kode Karyawan") + "\" berhasil dihapus");
        } else {
            System.out.println("Karyawan dengan nomor urutan tersebut tidak ditemukan.");
        }
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
