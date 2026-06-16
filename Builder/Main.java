/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package homework;

/**
 *
 * @author aruna
 */
public class Main {
    public static void main(String[] args) {
        PatientRecord p = new PatientRecord.Builder().setAge("45").setBillAmount("30000").setDisease("fever")
                .setDoctorName("xyz").setPatientName("abc").setRoomType("icu").Making();
        System.out.println(p);
    }
    
}
