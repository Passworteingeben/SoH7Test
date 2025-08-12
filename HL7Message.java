// import java.text.SimpleDateFormat;
// import java.util.Date;

public class HL7Message {

    private String applikation = "rv corp";
    private String KID;
    private String recApp = "kserver";
    private String controlId;
    private String pID;
    private String PName;
    private String fallID;
    private String date;

    public HL7Message(String KID,
                      String controlId,
                      String pID,
                      String PName,
                      String fallID,
                      String date) {
        this.KID = KID;
        this.controlId = controlId;
        this.pID = pID;
        this.PName = PName;
        this.fallID = fallID;
        this.date = date;
    }

    public String createHeader(String version) {
        // MSH|^~\&|SendingApp|SendingFacility|ReceivingApp|ReceivingFacility|DateTime||MessageType|ControlID|ProcessingID|Version
        StringBuilder msh = new StringBuilder();
        msh.append("MSH|^~\\&|")
           .append(applikation).append("|")
           .append(KID).append("|")
           .append(recApp).append("|")
           .append("|")
           .append(date).append("|")
           .append("|")
           .append("MSG|")
           .append(controlId).append("|")
           .append("P|")
           .append(version);
        return msh.toString();
    }

    public String createPID(String pID, String PName) {
        // PID|1|PatientID||PatientName
        return "PID|1|" + pID + "||" + PName;
    }

    public String createPV1(String fallID) {
        // PV1|1|FALLID
        return "PV1|1|" + fallID;
    }

    public String createMessage(String version) {
        StringBuilder message = new StringBuilder();
        message.append(createHeader(version)).append("\r");
        message.append(createPID(this.pID, this.PName)).append("\r");
        message.append(createPV1(this.fallID)).append("\r");
        switch (version) {
            case "1.1":
                message.append("ZXX|MockSegment|for|version|1.1");
                break;
            case "2.3":
                message.append("ZYY|MockSegment|for|version|2.3");
                break;
            default:
                message.append("ZZZ|MockSegment|for|version|" + version);
                break;
        }
        return message.toString();
    }

    public static void main(String[] args) {
        // String dateStr = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        // HL7Message hl7 = new HL7Message(
        //     "KID123",
        //     "CTRL456",
        //     "PID789",
        //     "John^Doe",
        //     "CASE001",
        //     dateStr
        // );
        // String msg = hl7.createMessage("2.3");
        // System.out.println(msg);
    }
}
