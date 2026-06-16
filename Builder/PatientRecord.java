
package homework;


public class PatientRecord 
{
    private String patientName;
    private String age;
    private String disease;
    private String doctorName;
    private String roomType;
    private String billAmount;

    
    @Override
    public String toString()
    {
        return patientName+" "+age+" "+disease+" "+doctorName+" "+roomType+" "+billAmount;
    }
    
    public static class Builder
    {
        private PatientRecord maker;
        
        public Builder()
        {
        maker = new PatientRecord();
        }
        
    public Builder setPatientName(String patientName) {
        maker.patientName = patientName;
        return this;
    }

    public Builder setAge(String age) {
        maker.age = age;
        return this;
    }

    public  Builder setDisease(String disease) {
        maker.disease = disease;
        return this;
    }

    public Builder setDoctorName(String doctorName) {
        maker.doctorName = doctorName;
        return this;
    }

    public Builder setRoomType(String roomType) {
        maker.roomType = roomType;
        return this;
    }

    public Builder setBillAmount(String billAmount) {
        maker.billAmount = billAmount;
        return this;
    }
    
    public PatientRecord Making()
    {
        
        return maker;
    }
        
    }
}
