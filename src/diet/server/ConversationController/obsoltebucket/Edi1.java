/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.ConversationController.obsoltebucket;

import diet.message.MessageChatTextFromClient;
import diet.server.Conversation;
import diet.server.ConversationController.DefaultConversationController;

import diet.server.Participant;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author gj
 */
public class Edi1 extends DefaultConversationController {

	public Edi1(Conversation c) {
		super(c);
		this.experimentHasStarted = true;

		JFrame jf = new JFrame("");
		JButton jb = new JButton("g");
		jf.getContentPane().add(jb);
		jf.setVisible(true);

	}

	public static boolean showcCONGUI() {
		return true;

	}

	@Override
	public void participantJoinedConversation(Participant p) {
		super.participantJoinedConversation(p); // To change body of generated
												// methods, choose Tools |
												// Templates.

		if (conversation.getParticipants().getAllParticipants().size() == 2) {
			participantPartnering.createNewSubdialogue(conversation.getParticipants().getAllParticipants());
			isTypingOrNotTyping.addPairWhoAreMutuallyInformedOfTyping(
					(Participant) conversation.getParticipants().getAllParticipants().elementAt(0),
					(Participant) conversation.getParticipants().getAllParticipants().elementAt(0));
		}

	}

	@Override
	public void processChatText(Participant sender, MessageChatTextFromClient mct) {
		Participant recip;// =
							// (Participant)this.pp.getRecipients(sender).elementAt(0);

		recip = (Participant) conversation.getParticipants().getAllOtherParticipants(sender).elementAt(0);

		isTypingOrNotTyping.addSpoofTypingInfo(recip, new Date().getTime());

		conversation.newsendInstructionToParticipant(sender, recip.getUsername() + " is other");

		super.processChatText(sender, mct); // To change body of generated
											// methods, choose Tools |
											// Templates.
	}

}
