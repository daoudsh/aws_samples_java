
# Amazon Web Services samples  
  
This repo consist of example of using AWS with Java SDK.   
All this examples suppose that you already installed and configured aws cli.   
  

 - [aws cli install](https://docs.aws.amazon.com/cli/latest/userguide/installing.html)
 - [aws cli config](https://docs.aws.amazon.com/cli/latest/topic/config-vars.html)

If you don't want to install AWS cli, replace loadClient method with

    private static void loadClient() {  
    
        BasicAWSCredentials creds = new BasicAWSCredentials(YOUR_ACCESS_KEY, YOUR_SECRET_KEY);  
        sns = AmazonSNSClient.builder().withRegion(Regions.EU_WEST_1)  
                .withCredentials(new AWSStaticCredentialsProvider(creds))  
                .build();  
    }
