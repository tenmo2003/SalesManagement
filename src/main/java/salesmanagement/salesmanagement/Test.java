package salesmanagement.salesmanagement;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Test {
    static int random(int r) {
        return 1 + (int) (Math.random() * r);
    }

    public static void main(String[] args) {
        List<String> lastName = new ArrayList<>(Arrays.asList("Ngũ Thành", "Bùi Đào Duy", "Đỗ Đức", "Ngô Phan Minh", "Nguyễn Việt", "Trần Đình Tuấn", "Phạm Xuân", "Nguyễn Trần Gia", "Lê Minh", "Nguyễn Đình", "Phạm Bá", "Sầm Anh", "Trương Tuấn", "Đỗ Minh", "Nguyễn Đăng", "Nguyễn Trần", "Đào Ngọc Hải", "Lê Quang", "Nguyễn Hữu", "Đỗ Minh", "Trương Minh", "Nguyễn Tuấn", "Nguyễn Thị Hồng", "Nguyễn Đăng", "Đoàn Thị Minh", "Nguyễn Thị", "Bàn Văn", "Phạm Minh", "Nguyễn Huy", "Lê Công", "Phùng Huy", "Trịnh Huy", "Vũ Phượng", "Hoàng Phi", "Đỗ Đức", "Dương Đức", "Nguyễn Khắc Nam", "Trần Quốc", "Nguyễn Đồng", "Nguyễn Việt", "Lương Thị Thu", "Nguyễn Văn", "Đào Vũ Minh", "Trịnh Văn", "Đặng Văn", "Lê Hải", "Nguyễn Ngọc", "Nguyễn Hải", "Nguyễn Tiến", "Trần Quý", "Phạm Hồng", "Nguyễn Hoài", "Hà Công", "Đỗ Tuấn", "Trần Tuấn", "Bùi Thị", "Phạm Khôi", "Đặng Trí", "Đinh Văn", "Trần Quốc", "Bùi Đặng Đức", "Võ Hồng", "Lê Duy", "Hoàng Mạnh", "Dương Hải", "Đỗ Minh", "Nguyễn An", "Nguyễn Đức", "Lý Trường", "Phạm Đức", "Trương Tấn", "Hoàng Huy", "Lại Đức", "Nguyễn Thịnh", "Bùi Thế", "Trương Thị Huyền", "Lê Bá", "Nguyễn Minh", "Hà Sơn", "Hoàng Thanh", "Phạm Tú", "Vũ Thị Thành", "Nguyễn Đình"));
        List<String> firstName = new ArrayList<>(Arrays.asList("An", "Anh", "Anh", "Anh", "Anh", "Anh", "Bách", "Bảo", "Châu", "Cường", "Danh", "Dũng", "Dũng", "Duy", "Dương", "Đạt", "Đăng", "Đông", "Đồng", "Đức", "Đức", "Đức", "Hà", "Hải", "Hằng", "Hiền", "Hiếu", "Hiếu", "Hiệu", "Hoàng", "Hoàng", "Hoàng", "Hồng", "Hùng", "Huy", "Huy", "Huy", "Huy", "Hưng", "Hưng", "Hương", "Khang", "Khánh", "Khánh", "Khởi", "Lâm", "Linh", "Long", "Mạnh", "Mạnh", "Minh", "Nam", "Nga", "Nghĩa", "Nghĩa", "Ngọc", "Nguyên", "Nhân", "Ninh", "Phi", "Phong", "Phúc", "Quang", "Quân", "Quyền", "Sáng", "Sơn", "Tân", "Thành", "Thành", "Thành", "Thắng", "Thắng", "Thuận", "Thuật", "Trâm", "Trường", "Tuấn", "Tùng", "Tùng", "Uyên", "Vinh", "Vũ"));
        List<String> birthDate = new ArrayList<>(Arrays.asList("11/05/2003", "08/02/2003", "11/24/2003", "12/03/2002", "8/15/2003", "10/01/2003", "8/25/2003", "23/05/2003", "04/04/2003", "11/25/2003", "11/20/2003", "01/11/2002", "18/10/2003", "06/08/2003", "07/05/2003", "01/08/2003", "2/20/2003", "19/04/2003", "09/08/2003", "10/14/2003", "4/22/2003", "10/12/2003", "04/06/2003", "9/30/2003", "02/08/2003", "05/11/2003", "17/03/2002", "03/11/2003", "03/11/2003", "9/29/2003", "06/09/2003", "9/29/2003", "27/04/2003", "12/27/2003", "04/12/2003", "06/04/2003", "08/09/2003", "09/07/2003", "26/11/2003", "7/30/2003", "11/01/2003", "11/08/2003", "10/20/2003", "01/10/2003", "18/04/2003", "2/25/2003", "25/12/2003", "8/25/2003", "24/09/2003", "04/05/2003", "09/12/2003", "14/10/2003", "03/05/2003", "12/19/2002", "6/20/2003", "9/20/2003", "11/16/2003", "1/21/2003", "7/18/2003", "02/09/2003", "21/10/2003", "12/17/2003", "9/29/2003", "13/09/2003", "07/11/2003", "04/03/2003", "7/29/2003", "09/10/2003", "27/07/2003", "9/26/2003", "02/12/2003", "1/15/2003", "4/24/2003", "1/16/2003", "17/09/2003", "2/19/2003", "02/03/2003", "03/07/2003", "7/17/2003", "03/06/2002", "08/04/2003", "6/29/2003", "07/09/2003"));
        List<String> birthDateF = new ArrayList<>();
        List<String> studentID = new ArrayList<>(Arrays.asList("21020271", "21020263", "21020274", "21021654", "21020277", "21020280", "21020283", "21020751", "21020286", "21020118", "21020289", "21020754", "21020058", "21020292", "21020757", "21020011", "21020301", "21020013", "21020760", "21020304", "21020307", "21020539", "21020310", "21020313", "21020068", "21020316", "21021656", "21020319", "21020071", "21020322", "21020763", "21020325", "21020764", "21020074", "21020124", "21020328", "21020542", "21020331", "21020766", "21020334", "21020337", "21020768", "21020020", "21020343", "21020770", "21020346", "21020774", "21020349", "21020777", "21020352", "21020023", "21020779", "21020127", "21020365", "21020549", "21020368", "21020083", "21020371", "21020362", "21020374", "21020781", "21020377", "21020380", "21020786", "21020386", "21020717", "21020389", "21020392", "21020790", "21020401", "21020095", "21020130", "21020404", "21020410", "21020796", "21020413", "21020416", "21020395", "21020398", "21021661", "21020419", "21020422", "21021677"));
        for (int i = 0; i < birthDate.size(); i++) {
            String dateString = birthDate.get(i);
            String[] dateParts = dateString.split("/");
            String day = dateParts[1];
            String month = dateParts[0];
            String year = dateParts[2];

            birthDateF.add(year + "-" + month + "-" + day);
        }
        for (int i = 0; i < lastName.size(); i++) {
            String q = "INSERT INTO employees (lastName, firstName, birthDate, gender, email, mailVerified, officeCode, reportsTo, jobTitle, username, password, phoneCode, phone, status, joiningDate, lastWorkingDate)\n" +
                    "VALUES ('" + lastName.get(i) + "', '" + firstName.get(i) + "', '" + birthDateF.get(i) + "', '" + ((random(4) > 2) ? "male" : "female") + "', '" + studentID.get(i) + "@vnu.edu.vn" + "', 1," + random(4) + "," + Integer.toString(21000000 + random(30 + i)) + ", '" + (random(30) > 20 ? "Employee" : (random(20) > 10 ? "Manager" : "HR")) + "', '" + studentID.get(i) + "', '12345678', '+84 (VN)', '0" + Integer.toString(random(899999999) + 100000000) + "', '" + (random(30) > 10 ? "ACTIVE" : "INACTIVE") + "', '" + (2015 + random(8)) + "-" + random(12) + "-" + Integer.toString(29) + "', NULL);";
            System.out.println(q+";");
        }
    }
}