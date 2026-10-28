import java.io.*;
import java.util.*;

class Project implements Serializable {
    int id;
    String clientName;
    String projectName;
    double amount;
    String paymentStatus;

    public Project(int id, String clientName, String projectName, double amount, String paymentStatus) {
        this.id = id;
        this.clientName = clientName;
        this.projectName = projectName;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
    }

    public void display() {
        System.out.println("ID: " + id);
        System.out.println("Client: " + clientName);
        System.out.println("Project: " + projectName);
        System.out.println("Amount: ₹" + amount);
        System.out.println("Status: " + paymentStatus);
        System.out.println("---------------------------");
    }
}

public class FreelancerSystem {
    static final String FILE_NAME = "projects.dat";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Freelancer Management System ===");
            System.out.println("1. Add Project");
            System.out.println("2. View Projects");
            System.out.println("3. Update Payment Status");
            System.out.println("4. View Pending Payments");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    addProject(sc);
                    break;
                case 2:
                    viewProjects();
                    break;
                case 3:
                    updatePayment(sc);
                    break;
                case 4:
                    viewPending();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    static void addProject(Scanner sc) {
        try {
            System.out.print("Enter ID: ");
            int id = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Client Name: ");
            String client = sc.nextLine();

            System.out.print("Enter Project Name: ");
            String project = sc.nextLine();

            System.out.print("Enter Amount: ");
            double amount = sc.nextDouble();

            Project p = new Project(id, client, project, amount, "Pending");

            FileOutputStream fos = new FileOutputStream(FILE_NAME, true);
            ObjectOutputStream oos = new ObjectOutputStream(fos) {
                protected void writeStreamHeader() throws IOException {
                    reset();
                }
            };

            oos.writeObject(p);
            oos.close();

            System.out.println("Project Added Successfully!");

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    static void viewProjects() {
        try {
            FileInputStream fis = new FileInputStream(FILE_NAME);
            ObjectInputStream ois = new ObjectInputStream(fis);

            while (true) {
                Project p = (Project) ois.readObject();
                p.display();
            }
        } catch (EOFException e) {
            // End of file
        } catch (Exception e) {
            System.out.println("No records found.");
        }
    }

    static void updatePayment(Scanner sc) {
        List<Project> list = new ArrayList<>();

        try {
            FileInputStream fis = new FileInputStream(FILE_NAME);
            ObjectInputStream ois = new ObjectInputStream(fis);

            while (true) {
                list.add((Project) ois.readObject());
            }
        } catch (EOFException e) {
        } catch (Exception e) {
        }

        System.out.print("Enter Project ID to update: ");
        int id = sc.nextInt();

        boolean found = false;

        for (Project p : list) {
            if (p.id == id) {
                p.paymentStatus = "Paid";
                found = true;
                System.out.println("Payment Updated!");
                break;
            }
        }

        if (!found) {
            System.out.println("Project not found!");
            return;
        }

        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME));
            for (Project p : list) {
                oos.writeObject(p);
            }
            oos.close();
        } catch (Exception e) {
            System.out.println("Error updating file.");
        }
    }

    static void viewPending() {
        try {
            FileInputStream fis = new FileInputStream(FILE_NAME);
            ObjectInputStream ois = new ObjectInputStream(fis);

            while (true) {
                Project p = (Project) ois.readObject();
                if (p.paymentStatus.equals("Pending")) {
                    p.display();
                }
            }
        } catch (EOFException e) {
        } catch (Exception e) {
            System.out.println("No pending records.");
        }
    }
}
