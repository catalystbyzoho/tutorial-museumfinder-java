
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;

import com.catalyst.Context;
import com.catalyst.basic.BasicIO;
import com.catalyst.basic.ZCFunction;
import com.zc.common.ZCProject;
import com.zc.component.mail.ZCMail;
import com.zc.component.mail.ZCMailContent;

public class Mailer implements ZCFunction {
	
	private static final Logger LOGGER = Logger.getLogger(Mailer.class.getName());
	
	private static final String SUBJECT = "Museum Search Results";
	
	private static final String FROM_MAIL = "ENTER_YOUR_FROM_MAIL_ID_HERE"; //Enter the sender's email address you configured earlier
	
	@Override
    public void runner(Context context, BasicIO basicIO) throws Exception {
		try {
			ZCProject.initProject();
			Object content = basicIO.getArgument("content"); //Obtains the final list from the MergeDataSet function
			Object mailId = basicIO.getArgument("mail_id"); //Obtains the user's email address from the provided input
			if(content != null) {
				
				String mailContent = content.toString();
				ZCMailContent mail = ZCMailContent.getInstance();
				mail.setSubject(SUBJECT);
				mail.setFromEmail(FROM_MAIL);
				if(mailId != null) {
					mail.setToEmail(mailId.toString());
				}
				else {
					basicIO.write(constructErrorMessage("mail_id not found"));
					basicIO.setStatus(400);
					return;
				}
				mail.setContent(mailContent);
				mail.setHtmlMode(false);
				ZCMail.getInstance().sendMail(mail);
				basicIO.setStatus(200);
				basicIO.write(constructSuccessMessage("Mail has been sent to "+mailId+" successfully"));
			}
		}
		catch(Exception e) {
			LOGGER.log(Level.SEVERE,"Exception in Mailer",e); //You can view this log from Logs in the Catalyst console
			basicIO.setStatus(500);
			basicIO.write(constructErrorMessage(e.getMessage()));
		}
	}
	
	private JSONObject constructErrorMessage(String message) {
		
		JSONObject errorJSON = new JSONObject();
		errorJSON.put("status", "failure");
		errorJSON.put("message", message);
		return errorJSON;
	}
	
	private JSONObject constructSuccessMessage(String message) {
		
		JSONObject errorJSON = new JSONObject();
		errorJSON.put("status", "success");
		errorJSON.put("message", message);
		return errorJSON;
	}
}
