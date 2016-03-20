package diet.server.ConversationController;

import diet.attribval.AttribVal;
import diet.message.MessageChatTextFromClient;
import diet.message.MessageKeypressed;
import diet.message.MessageTask;
import diet.message.MessageWYSIWYGDocumentSyncFromClientInsert;
import diet.message.MessageWYSIWYGDocumentSyncFromClientRemove;
import diet.server.ConnectionListener;
import diet.server.Conversation;
import diet.server.ConversationController.ui.CustomDialog;
import diet.server.Participant;
import diet.task.mazegame.MazeGameController2WAY;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.Vector;

public class EDI2016DyadicSingleOrSplitScreen extends DefaultConversationController {

	public static boolean showcCONGUI() {
		return true;
	}

	public EDI2016DyadicSingleOrSplitScreen(Conversation c) {
		super(c);
		String portNumberOfServer = "" + ConnectionListener.staticGetPortNumber();
		config.param_experimentID = "DefaultMultiPartyConversation";

		String[] interfacechoice = { "single", "split" };

		String option = CustomDialog.show2OptionDialog(interfacechoice, "Choose an interface", "");
		if (option.equalsIgnoreCase("single")) {
			super.config.client_numberOfWindows = 1;
		} else {
			super.config.client_numberOfWindows = 2;
		}

	}

	@Override
	public boolean requestParticipantJoinConversation(String participantID) {

		return true;

	}

	@Override
	public synchronized void participantJoinedConversation(final Participant p) {
		super.participantJoinedConversation(p);
		if (conversation.getParticipants().getAllParticipants().size() == 2) {
			participantPartnering.createNewSubdialogue(conversation.getParticipants().getAllParticipants());
			this.isTypingOrNotTyping.setWhoSeesEachOthersTyping(participantPartnering);
		}

	}

	@Override
	public void participantRejoinedConversation(Participant p) {
		super.participantRejoinedConversation(p);
	}

	public synchronized void processTaskMove(MessageTask mtm, Participant origin) {
	}

	@Override
	public synchronized void processChatText(Participant sender, MessageChatTextFromClient mct) {
		this.isTypingOrNotTyping.processTurnSentByClient(sender);

		Vector additionalData = new Vector();
		additionalData.addElement(new AttribVal("splitscreen", "split"));
		conversation.newrelayTurnToPermittedParticipants(sender, mct, additionalData);

	}

	@Override
	public void processKeyPress(Participant sender, MessageKeypressed mkp) {
		// super.processKeyPress(sender, mkp);
		if (super.config.client_numberOfWindows == 1)
			this.isTypingOrNotTyping.processDoingsByClient(sender);

	}

	@Override
	public void processWYSIWYGTextInserted(Participant sender, MessageWYSIWYGDocumentSyncFromClientInsert mWYSIWYGkp) {
		if (super.config.client_numberOfWindows == 1)
			return;
		Participant recipient = (Participant) participantPartnering.getRecipients(sender).elementAt(0);
		Vector additionalValues = this.getAdditionalInformationForParticipant(sender);
		conversation.changeClientInterface_clearMainWindowsExceptWindow0(recipient);
		conversation.newsendArtificialTurnFromApparentOrigin(sender, recipient, mWYSIWYGkp.getAllTextInWindow(), 1,
				additionalValues);

	}

	@Override
	public void processWYSIWYGTextRemoved(Participant sender, MessageWYSIWYGDocumentSyncFromClientRemove mWYSIWYGkp) {
		if (super.config.client_numberOfWindows == 1)
			return;
		Participant recipient = (Participant) participantPartnering.getRecipients(sender).elementAt(0);
		Vector additionalValues = this.getAdditionalInformationForParticipant(sender);
		conversation.changeClientInterface_clearMainWindowsExceptWindow0(recipient);
		conversation.newsendArtificialTurnFromApparentOrigin(sender, recipient, mWYSIWYGkp.getAllTextInWindow(), 1,
				additionalValues);

	}

}
