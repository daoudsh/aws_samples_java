package de.shiyar.aws.sns;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AwsSNSTemplateEmail {

    private static Properties prop = new Properties();

    private static String SENDER = null;
    private static String RECIPIENT = null;
    private static String SUBJECT = null;
    private static String templateName = null;
    private static String emailText = null;
    private static String dataHolder = null;

    private static AmazonSimpleEmailService ses;

    public static void main(String[] args) throws Exception {
        loadProperties();
        loadClient();

        try {
            createTemplate();
            sendEmail();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            deleteTemplate();
        }
    }

    private static void createTemplate() {
        Template template = new Template();
        template.setTemplateName(templateName);
        template.setSubjectPart(SUBJECT);
        template.setTextPart(emailText);
        CreateTemplateRequest createTemplateRequest = new CreateTemplateRequest();
        createTemplateRequest.setTemplate(template);
        CreateTemplateResult result = ses.createTemplate(createTemplateRequest);
        System.out.println(result.getSdkResponseMetadata());
    }

    private static void deleteTemplate() {
        DeleteTemplateRequest deleteTemplateRequest = new DeleteTemplateRequest();
        deleteTemplateRequest.setTemplateName(templateName);
        DeleteTemplateResult result = ses.deleteTemplate(deleteTemplateRequest);
        System.out.println(result.getSdkResponseMetadata());
    }

    private static void sendEmail() {
        Destination destination = new Destination();
        List<String> toAddresses = new ArrayList<>();
        toAddresses.add(RECIPIENT);
        destination.setToAddresses(toAddresses);
        SendTemplatedEmailRequest templateEmailRequest = new SendTemplatedEmailRequest();
        templateEmailRequest.withDestination(destination);
        templateEmailRequest.withTemplate(templateName);
        templateEmailRequest.withTemplateData(dataHolder);
        templateEmailRequest.withSource(SENDER);

        SendTemplatedEmailResult templateEmailResult = ses.sendTemplatedEmail(templateEmailRequest);
        System.out.println(templateEmailResult.getMessageId());
    }

    private static void loadClient()  {

        ses = AmazonSimpleEmailServiceClientBuilder.standard()
                .withRegion(Regions.EU_WEST_1).build();
    }

    private static void loadProperties() throws IOException {
        InputStream input = AwsSNSTemplateEmail.class.getClassLoader().getResourceAsStream("application.properties");
        prop.load(input);
        SENDER = prop.getProperty("sns.email.sender");
        RECIPIENT = prop.getProperty("sns.email.recipient");
        SUBJECT = prop.getProperty("sns.email.subject");
        templateName = prop.getProperty("sns.email.templateName");
        emailText = prop.getProperty("sns.email.emailText");
        dataHolder = prop.getProperty("sns.email.dataHolder");
    }
}
