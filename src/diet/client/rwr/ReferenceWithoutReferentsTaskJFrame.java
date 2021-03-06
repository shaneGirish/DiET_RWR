package diet.client.rwr;

import diet.client.ConnectionToServer;
import diet.message.referenceWithoutReferentsTask.*;
import diet.message.referenceWithoutReferentsTask.ReferenceWithoutReferentsTaskMessage.MessageType;
import diet.server.ConversationController.rwr.PlayerType;
import diet.server.Participant;

import java.awt.*;
import java.awt.event.ItemEvent;
import javax.swing.*;

import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.YES_OPTION;

public class ReferenceWithoutReferentsTaskJFrame extends JFrame {
    private final PlayerType playerType;
    private final String email;
    private final String username;
    private final ConnectionToServer connectionToServer;
    private final CardsPanel cardsPanel;
    private final ControlsPanel controlsPanel;
    private final int numberOfCards;

    public ReferenceWithoutReferentsTaskJFrame(String email, String username, PlayerType playerType, int numberOfCards,
            ConnectionToServer connectionToServer) {
        super(playerType.name());

        this.email = email;
        this.username = username;

        this.connectionToServer = connectionToServer;
        this.playerType = playerType;

        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        this.numberOfCards = numberOfCards;
        this.cardsPanel = new CardsPanel(playerType, numberOfCards, this::sendOrderedListOfCardIds);
        this.controlsPanel = new ControlsPanel(this::onReadyStateChange);

        this.add(cardsPanel, BorderLayout.NORTH);
        this.add(controlsPanel, BorderLayout.SOUTH);

        SwingUtilities.invokeLater(this::sendOrderedListOfCardIds);

        this.pack();
        this.setResizable(false);
        this.setVisible(true);
    }

    public void resetTurn(boolean shouldRandomizeCardOrder) {
        if (shouldRandomizeCardOrder) {
            cardsPanel.reset();
        }
        controlsPanel.reset();
        SwingUtilities.invokeLater(this::sendOrderedListOfCardIds);
        this.pack();
        this.repaint();
    }

    private void sendOrderedListOfCardIds() {
        connectionToServer.sendMessage(new ReferenceWithoutReferentsCardMoveMessage(email, username, playerType,
                cardsPanel.getOrderedListOfCardIds()));
    }

    private void onReadyStateChange(ItemEvent itemEvent) {
        boolean readyState = itemEvent.getStateChange() == ItemEvent.SELECTED;
        connectionToServer
                .sendMessage(new ReferenceWithoutReferentsReadyStateMessage(email, username, playerType, readyState));
    }

    public void requestFinalInput() {
        this.setVisible(false);
        JPanel optionPaneContent = new JPanel(new BorderLayout());
        JTextArea inputField = new JTextArea();
        inputField.setRows(5);
        JScrollPane inputPanel = new JScrollPane(inputField);

        JPanel labelPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        labelPanel.add(new JLabel("    "), gridBagConstraints);
        gridBagConstraints.gridy = 1;
        labelPanel.add(new JLabel("What do you think were the " + numberOfCards + " objects you were arranging?"),
                gridBagConstraints);
        gridBagConstraints.gridy = 2;
        labelPanel.add(new JLabel("Try to describe as many of the objects from the game as you can remember."),
                gridBagConstraints);
        gridBagConstraints.gridy = 3;
        labelPanel.add(new JLabel("    "), gridBagConstraints);

        optionPaneContent.add(labelPanel, BorderLayout.NORTH);
        optionPaneContent.add(inputPanel, BorderLayout.SOUTH);

        String input;
        while (true) {
            JOptionPane.showConfirmDialog(null, optionPaneContent, "Success", JOptionPane.OK_CANCEL_OPTION);
            input = inputField.getText();
            input = input == null ? "" : input;
            int selectedOption = JOptionPane.showConfirmDialog(null, "Your response was: \n" + input + "",
                    "Confirm your response.", JOptionPane.OK_CANCEL_OPTION, INFORMATION_MESSAGE);
            if (selectedOption == YES_OPTION) {
                connectionToServer.sendMessage(new ReferenceWithoutReferentsFinalInputMessage(email, username, input));
                JOptionPane.showMessageDialog(null, "Thank you for your time!", "Game complete",
                        JOptionPane.PLAIN_MESSAGE);
                break;
            }
        }
        this.setVisible(true);
    }
}
