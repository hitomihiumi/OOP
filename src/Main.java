
public class Main {
    public static void main(String[] args) {
        try {
            DataRecord record = new DataRecord();
            record.setFirstName("  Василь   ");
            record.setLastName("   Чаковський   ");
            record.setEmail(" rjbgek@gmail.com  ");
            record.setPhoneNumber("  123-456-7890 ");
            record.setAddress(" 123 якась вулиця ");
            record.setCity("  якесь місто ");
            record.setCountry("  Україна ");
            record.setPostalCode(" 12312  ");
            record.setCompany("  тест  ");
            record.setDepartment("  тест  ");

            System.out.println("До прибирання пробілів:");
            System.out.println("Им'я: '" + record.getFirstName() + "'");
            System.out.println("Фамілія: '" + record.getLastName() + "'");
            System.out.println("Email: '" + record.getEmail() + "'");

            ReflectionUtils.trimStringProperties(record);

            System.out.println("\nПісля прибирання пробілів:");
            System.out.println("Им'я: '" + record.getFirstName() + "'");
            System.out.println("Фамілія: '" + record.getLastName() + "'");
            System.out.println("Email: '" + record.getEmail() + "'");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
