package de.shiyar.aws.sns;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.GetSMSAttributesRequest;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.SetSMSAttributesRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;


public class AwsSNSShortMessageServiceSMS {

    private static AmazonSNS sns;
    private static Properties prop = new Properties();

    private static String MESSAGE = null;
    private static String PHONE_NUMBER = null;
    private static String SENDER_ID = null;
    private static String AWS_ROLE = null;
    private static String SMS_TYPE = null;

    public static void main(String[] args) throws IOException {
        loadProperties();
        loadClient();
        setDefaultSmsAttributes(sns);
        sendSMSMessage(sns, MESSAGE, PHONE_NUMBER);
    }

    public static void sendSMSMessage(AmazonSNS snsClient, String message, String phoneNumber) {
        PublishResult result = snsClient.publish(new PublishRequest()
                .withMessage(message)
                .withPhoneNumber(phoneNumber));
        System.out.println(result);
    }

    private static void setDefaultSmsAttributes(AmazonSNS snsClient) {
        SetSMSAttributesRequest setRequest = new SetSMSAttributesRequest()
                .addAttributesEntry("DefaultSenderID", SENDER_ID)
                .addAttributesEntry("DeliveryStatusIAMRole", AWS_ROLE)
                .addAttributesEntry("DefaultSMSType", SMS_TYPE);
        snsClient.setSMSAttributes(setRequest);
        Map<String, String> myAttributes = snsClient.getSMSAttributes(new GetSMSAttributesRequest())
                .getAttributes();
        System.out.println("SMS attributes:");
        for (String key : myAttributes.keySet()) {
            System.out.println(key + " = " + myAttributes.get(key));
        }
    }

    private static void loadClient() {

        sns = AmazonSNSClient.builder().withRegion(Regions.EU_WEST_1).build();
    }

    private static void loadProperties() throws IOException {
        InputStream input = AwsSNSTemplateEmail.class.getClassLoader().getResourceAsStream("application.properties");
        prop.load(input);
        SENDER_ID = prop.getProperty("sns.sms.senderId");
        MESSAGE = prop.getProperty("sns.sms.message");
        PHONE_NUMBER = prop.getProperty("sns.sms.senderNumber");
        AWS_ROLE = prop.getProperty("sns.sms.awsRole");
        SMS_TYPE = prop.getProperty("sns.sms.type");

    }
}
