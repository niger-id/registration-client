
package  registrationtest.testcases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testfx.api.FxRobot;
import org.testfx.api.FxRobotContext;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import io.mosip.registration.config.AppConfig;
import io.mosip.registration.controller.Initialization;
import io.mosip.registration.dao.RegistrationDAO;
import io.mosip.registration.dao.impl.RegistrationDAOImpl;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.VerticalDirection;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import registrationtest.controls.Alerts;
import registrationtest.controls.Buttons;
import registrationtest.pages.AuthenticationPage;
import registrationtest.pages.BiometricUploadPage;
import registrationtest.pages.DemographicPage;
import registrationtest.pages.EodApprovalPage;
import registrationtest.pages.HomePage;
import registrationtest.pages.LoginPage;
import registrationtest.pages.SelectLanguagePage;
import registrationtest.pages.UploadPacketPage;
import registrationtest.pages.WebViewDocument;
import registrationtest.pojo.output.*;
import registrationtest.pojo.schema.Root;
import registrationtest.pojo.schema.Schema;
import registrationtest.runapplication.RegistrationMain;
import  registrationtest.utility.ExtentReportUtil;
import registrationtest.utility.JsonUtil;
import  registrationtest.utility.PropertiesUtil;
import registrationtest.utility.RobotActions;
import registrationtest.utility.WaitsUtil;

import org.apache.log4j.LogManager; 

import org.apache.log4j.Logger;


/***
 * 
 * @author Neeharika.Garg
 * Login and Logout RegClient	
 * Steps Run this using Junit
 * First start method invokes and this will launch Registration Client and through dependency injection
 * 
 * Fxrobot will take control of primary stage and perform keyboard and mouse driven activities.
 *
 */
public class NewReg {
	private static final Logger logger = LogManager.getLogger(NewReg.class);  
	FxRobot robot;
	Schema schema;
	Root root; 
	Scene scene;
	Node node;
	Boolean flagContinueBtnFileUpload=true;
	Boolean flagContinueBtnBioUpload=true;
	private static ApplicationContext applicationContext;
	private static Stage applicationPrimaryStage;
	private static String upgradeServer = null;
	private static String tpmRequired = "Y";
	LoginPage loginPage;
	HomePage homePage;
	PropertiesUtil propertiesUtil;
	FxRobotContext context;
	Boolean result;
	DemographicPage demographicPage;
	BiometricUploadPage biometricUploadPage;
	Buttons buttons;
	WebViewDocument webViewDocument;
	RID rid1,rid2;
	AuthenticationPage authenticationPage;
	RobotActions robotActions;
	EodApprovalPage eodApprovalPage;
	UploadPacketPage uploadPacketPage;
	SelectLanguagePage selectLanguagePage;
	Alerts alerts;
	
	public boolean initialRegclientSet(FxRobot robot,String loginUserid,String loginPwd,Stage applicationPrimaryStage1)
	{
		boolean flag = false;
		try {
			ExtentReportUtil.ExtentSetting();
			ExtentReportUtil.test1=ExtentReportUtil.reports.createTest("Verify Initial Application start");
			ExtentReportUtil.step1=ExtentReportUtil.test1.createNode("STEP 1-Loading RegClient");
			
			loginPage=new LoginPage(robot);
			buttons=new Buttons(robot);
			authenticationPage=new AuthenticationPage(robot);	
			robotActions=new RobotActions(robot);
			webViewDocument=new WebViewDocument(robot);
			biometricUploadPage=new BiometricUploadPage(robot);
			alerts=new Alerts(robot);
			rid1=new RID();
			rid2=new RID();
			result=false;

			//Load Login screen
			loginPage.loadLoginScene(applicationPrimaryStage1);
			ExtentReportUtil.step1.log(Status.PASS, "RegclientScreen Loaded");
			
			ExtentReportUtil.step2=ExtentReportUtil.test1.createNode("STEP 2-Operator Enter Details ");
			
			//Enter userid and password
			loginPage.setUserId(loginUserid);
			 flag=loginPage.verifyAuthentication(loginPwd, applicationPrimaryStage1);
			ExtentReportUtil.step2.log(Status.PASS, "Operator logs in");
			
			if(flag==true)
			{
				ExtentReportUtil.test1.log(Status.PASS, "SUCCESS Restart Application");
			}
			else
			{
				ExtentReportUtil.test1.log(Status.FAIL, "FAIL");
			}
			
		}
		catch(Exception e)
		{

			logger.error(e.getMessage());
		}
		
		return flag;
	}
	public RID newRegistration(FxRobot robot,String loginUserid,String loginPwd,String supervisorUserid,
			String supervisorUserpwd,Stage applicationPrimaryStage1,String jsonContent,String process,String ageGroup,String fileName
			,ApplicationContext applicationContext)  {

		try {
		logger.info("New Adult Registration Scenario : " + process +" FileName : " + fileName);
		ExtentReportUtil.test1=ExtentReportUtil.reports.createTest("New Registration Scenario : " + process +" FileName : " + fileName);
		
			loginPage=new LoginPage(robot);
		buttons=new Buttons(robot);
		authenticationPage=new AuthenticationPage(robot);	
		robotActions=new RobotActions(robot);
		selectLanguagePage=new SelectLanguagePage(robot);
		
		rid1=null;
		rid2=null;
result=false;
		//Load Login screen
		buttons.clickcancelBtn();
		loginPage.loadLoginScene(applicationPrimaryStage1);
		
		
		//Enter userid and password
		
		
		
		loginPage.selectAppLang();
		
		loginPage.setUserId(loginUserid);
		
		homePage=loginPage.setPassword(loginPwd);
		
		//New Registration
		homePage.clickHomeImg();
		ExtentReportUtil.test1.info("Operator Logs in");
		if(PropertiesUtil.getKeyValue("sync").equals("Y"))
		homePage.clickSynchronizeData();
		
		demographicPage=homePage.clickNewRegistration();
		
		if(PropertiesUtil.getKeyValue("multilang").equals("Y"))
		{
		selectLanguagePage.selectLang();
		buttons.clicksubmitBtn();
		}	
		
		
		webViewDocument=demographicPage.screensFlow(jsonContent,process,ageGroup);

		
		buttons.clicknextBtn();

		
		rid1=webViewDocument.acceptPreview(process);

		buttons.clicknextBtn();

		if(!rid1.rid.trim().isEmpty())
		{
			ExtentReportUtil.test1.info("Demo, Doc, Bio - Done");
			ExtentReportUtil.test1.info("Preview done");
		}	else
			{	ExtentReportUtil.test1.info("Preview not valid");	
			}
		/**
		 * Authentication enter password
		 * Click Continue 
		 */
		authenticationPage.enterUserName(loginUserid);
		authenticationPage.enterPassword(loginPwd);
		
		buttons.clickAuthenticateBtn();


try {
	
	List<String> exceptionFlag=JsonUtil.JsonObjArrayListParsing(jsonContent,"bioExceptionAttributes");	
	if(exceptionFlag!=null)
			 {
				 /**
					 * Reviewer enter password
					 * Click Continue 
					 */
					authenticationPage.enterUserName(PropertiesUtil.getKeyValue("reviewerUserid"));
					authenticationPage.enterPassword(PropertiesUtil.getKeyValue("reviewerpwd"));
					buttons.clickAuthenticateBtn();

			 }
		
}catch(Exception e)
{
	logger.error("",e);
}
		
		/**
		 * Click Home, eodapprove, approval Button, authenticate button
		 * Enter user details
		 */
		rid2=webViewDocument.getacknowledgement(process);
		
		homePage.clickHomeImg();
		
		
		eodApprovalPage=homePage.clickeodApprovalImageView( applicationPrimaryStage, scene);
		eodApprovalPage.clickOnfilterField();
		eodApprovalPage.enterFilterDetails(rid1.getRid());
		eodApprovalPage.clickOnApprovalBtn();
		authenticationPage=eodApprovalPage.clickOnAuthenticateBtn();
		authenticationPage.enterUserName(supervisorUserid);
		authenticationPage.enterPassword(supervisorUserpwd);
		authenticationPage.clicksubmitBtn();
		robotActions.clickWindow();
		homePage.clickHomeImg();	
		buttons.clickConfirmBtn();
		if(!rid2.rid.trim().isEmpty())
		{
			ExtentReportUtil.test1.info("Approve Packet done");
		assertEquals(rid1.getRid(), rid2.getRid());
		}else
		{	ExtentReportUtil.test1.info("Approve Packet invalid");	
		
		}
		
		/**
		 * Upload the packet
		 */
		if(PropertiesUtil.getKeyValue("upload").equals("Y"))
			{
			

		uploadPacketPage=homePage.clickuploadPacketImageView( applicationPrimaryStage, scene);
		uploadPacketPage.selectPacket(rid1.getRid());
		buttons.clickuploadBtn();
		/**
		 * Verify Success Upload
		 */
		result=uploadPacketPage.verifyPacketUpload(rid1.getRid());
		ExtentReportUtil.test1.info( "Upload Packet done");
		}
		else if(PropertiesUtil.getKeyValue("upload").equals("N")){
			result=true;
		}
		//Logout Regclient
		rid1.appidrid=rid1.getAppidrid(applicationContext, rid1.rid);
		rid1.setResult(result);
		}catch(Exception e)
		{

			logger.error("",e);
			
			try {
				ExtentReportUtil.test1.addScreenCaptureFromPath(WaitsUtil.capture());
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
			
		}
		try
		{
			homePage.clickHomeImg();	
			buttons.clickConfirmBtn();
		}
			catch(Exception e)
			{
				logger.error("",e);
			}
			try {
				loginPage.logout();
				buttons.clickConfirmBtn();

			}
			catch(Exception e)
			{
				logger.error("",e);
			}

		
		if(result==true)
		{
			ExtentReportUtil.test1.log(Status.PASS, "TESTCASE PASS\n" +"[Appid="+ rid1.rid +"] [RID="+ rid1.appidrid +"] [DATE TIME="+ rid1.ridDateTime +"] [ENVIRONMENT=" +System.getProperty("mosip.hostname")+"]");
			ExtentReportUtil.test1.info("Approve Packet Details Below" + rid2.getWebViewAck());
		}		else
		{
			ExtentReportUtil.test1.log(Status.FAIL, "TESTCASE FAIL");
		
		}
		
		

		ExtentReportUtil.test1.info("Test Data Below" + jsonContent);
		ExtentReportUtil.reports.flush();
		
		
				
return rid1;
	}


	public RID loginlogout(FxRobot robot2, String loginUserid, String loginPwd, String supervisorUserid,
			String supervisorUserpwd, Stage primaryStage, JSONObject idjson, HashMap<String, String> documentUpload,
			String lang, String schemaversion, String jsonObjName, String idJsonPath) throws InterruptedException {
		
		loginPage=new LoginPage(robot);
		buttons=new Buttons(robot);
		authenticationPage=new AuthenticationPage(robot);	
		robotActions=new RobotActions(robot);

		//Load Login screen
		//loadLoginScene verb
		loginPage.loadLoginScene(primaryStage);

		//Enter userid and password
		loginPage.setUserId(loginUserid);


		homePage=loginPage.setPassword(loginPwd);

		//Logout Regclient
		loginPage.logout();
		
		return null;
	}
}
