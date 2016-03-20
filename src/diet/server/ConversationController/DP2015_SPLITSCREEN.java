/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.ConversationController;

import diet.attribval.AttribVal;
import diet.message.MessageChatTextFromClient;
import diet.message.MessageWYSIWYGDocumentSyncFromClientInsert;
import diet.message.MessageWYSIWYGDocumentSyncFromClientRemove;
import diet.server.Conversation;
import diet.server.ConversationController.ui.CustomDialog;
import diet.server.ConversationController.ui.JInterfaceSinglePressButtonFIVE;
import diet.server.Participant;
import diet.task.mazegame.MazeGameController2WAY;
import java.util.Vector;

/**
 *
 * @author gj
 */
public class DP2015_SPLITSCREEN extends MazeGameConversationControllerMultipleDyads {

	boolean evenMazesAreSplitScreen = true;

	public DP2015_SPLITSCREEN(Conversation c) {
		super(c);
		config.param_experimentID = "DP2015SPLITSCREEN";
		super.config.client_numberOfWindows = 2;

	}

	@Override
	public void startexperiment(boolean shuffle) {
		evenMazesAreSplitScreen = CustomDialog.getBoolean("Do you want even or odd mazes to be splitscreen?", "even",
				"odd");

		super.startexperiment(shuffle);

	}

	@Override
	public synchronized void processChatText(Participant sender, MessageChatTextFromClient mct) {
		if (!this.experimentHasStarted) {
			conversation.newsendInstructionToParticipant(sender, "Please wait for the experiment to start");
			return;
		}

		this.isTypingOrNotTyping.processTurnSentByClient(sender);
		MazeGameController2WAY mgcNEW = this.getMazeGameController(sender);
		Vector additionalData = mgcNEW.getAdditionalData(sender);
		if (doSplitScreen(sender)) {
			additionalData.addElement(new AttribVal("splitscreen", "split"));
			conversation.newrelayTurnToPermittedParticipants(sender, mct, additionalData);
			mgcNEW.appendToUI(sender.getUsername() + ": " + mct.getText());
		} else {
			additionalData.addElement(new AttribVal("splitscreen", "normal"));
			conversation.newrelayTurnToPermittedParticipants(sender, mct, additionalData);
			mgcNEW.appendToUI(sender.getUsername() + ": " + mct.getText());
			Participant pRecip = (Participant) participantPartnering.getRecipients(sender).elementAt(0);
			conversation.changeClientInterface_clearMainWindowsExceptWindow0(pRecip);

		}

	}

	@Override
	public void processWYSIWYGTextRemoved(Participant sender, MessageWYSIWYGDocumentSyncFromClientRemove mWYSIWYGkp) {
		if (!this.experimentHasStarted)
			return;
		if (doSplitScreen(sender) && this.experimentHasStarted) {
			Participant recipient = (Participant) participantPartnering.getRecipients(sender).elementAt(0);
			Vector additionalValues = this.getAdditionalInformationForParticipant(sender);
			conversation.changeClientInterface_clearMainWindowsExceptWindow0(recipient);
			conversation.newsendArtificialTurnFromApparentOrigin(sender, recipient, mWYSIWYGkp.getAllTextInWindow(), 1,
					additionalValues);
		}

	}

	@Override
	public void processWYSIWYGTextInserted(Participant sender, MessageWYSIWYGDocumentSyncFromClientInsert mWYSIWYGkp) {
		if (!this.experimentHasStarted)
			return;
		if (doSplitScreen(sender) && this.experimentHasStarted) {
			System.err.println("THE TEXT TYPED IS: " + mWYSIWYGkp.getTextTyped());
			Participant recipient = (Participant) participantPartnering.getRecipients(sender).elementAt(0);
			Vector additionalValues = this.getAdditionalInformationForParticipant(sender);
			conversation.changeClientInterface_clearMainWindowsExceptWindow0(recipient);
			conversation.newsendArtificialTurnFromApparentOrigin(sender, recipient, mWYSIWYGkp.getAllTextInWindow(), 1,
					additionalValues);
		}
	}

	public boolean doSplitScreen(Participant p) {
		MazeGameController2WAY mgc = this.getMazeGameController(p);
		int mazeno = mgc.getMazeNo();
		if (mazeno % 2 == 0) {
			if (this.evenMazesAreSplitScreen)
				return true;
			return false;
		} else {
			if (this.evenMazesAreSplitScreen)
				return false;
			return true;
		}

	}

}
