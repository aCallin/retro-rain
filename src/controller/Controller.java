package controller;

import java.awt.event.ActionEvent;
import java.util.LinkedList;

import javax.swing.event.ChangeEvent;

import controller.util.TrackPlayer;
import model.AudioFileList;
import model.Track;
import model.util.AudioFileLoader;
import view.MainUI;
import view.UserSelectionPanel;
import view.util.ViewResources;
import view.util.ControlButton;
import view.util.EDTDispatcher;

/**
 * Acts as the messenger between the view and the model portions of the program.
 * 
 * @author Adam
 */
public class Controller {
	private AudioFileList audioFileList;
	private MainUI mainUI;
	
	private LinkedList<UserSelectionPanel> userSelections;
	
	/**
	 * Creates a new <code>Controller</code> instance.
	 */
	public Controller() {
		userSelections = new LinkedList<UserSelectionPanel>();
	}
	
	/**
	 * Invokes the creation of the view and model and sets up relaying communication between them.
	 */
	public void start() {
		initializeModel();
		initializeView();
		
		EDTDispatcher.doAndWait(() -> {
			addAudioSelections();
			mainUI.getOverlayPanel().getControlPanel().getControlButton().addActionListener((ActionEvent e) -> onMasterControlButtonPressed());
			mainUI.getOverlayPanel().getControlPanel().getVolumeSlider().addChangeListener((ChangeEvent e) -> onMasterVolumeSliderChange());
			mainUI.setVisible(true);
		});
	}
	
	/**
	 * Loads all audio files and dumps them into an <code>AudioFileList</code> object.
	 */
	private void initializeModel() {
		audioFileList = new AudioFileList();
		AudioFileLoader.get().loadFromDirectory("res/audio", new String[] {".wav"}, audioFileList);
	}
	
	/**
	 * Loads view resources and creates the main UI.
	 */
	private void initializeView() {
		ViewResources.get().loadImagesFromDirectory("res/images", new String[] {".png"});
		ViewResources.get().loadFontsFromDirectory("res/fonts", new String[] {".ttf"});
		ControlButton.init();
		mainUI = new MainUI();
		EDTDispatcher.doAndWait(() -> mainUI.create());
	}
	
	/**
	 * Adds user selections to the content panel representing each of the loaded audio files.
	 */
	private void addAudioSelections() {
		for (String audioFileIdentifier : audioFileList.getAllAudioFileIdentifiers()) {
			UserSelectionPanel userSelection = new UserSelectionPanel(audioFileIdentifier);
			userSelection.getControlButton().addActionListener((ActionEvent e) -> onControlButtonPressed(userSelection));
			userSelection.getVolumeSlider().addChangeListener((ChangeEvent e) -> onVolumeSliderChange(userSelection));
			mainUI.getOverlayPanel().getContentPanel().addUserSelection(userSelection);
		}
	}
	
	/**
	 * Plays / stops audio whenever a control button is pressed.
	 * @param userSelection the user selection containing the control button acted on.
	 * @param trackPlayer the <code>TrackPlayer</code> object associated with the given user selection.
	 */
	private void onControlButtonPressed(UserSelectionPanel userSelection) {
		if (mainUI.getOverlayPanel().getControlPanel().getControlButton().getMode() == ControlButton.ControlMode.PAUSE) {
			userSelections.clear();
			mainUI.getOverlayPanel().getControlPanel().getControlButton().switchMode();
		}
		
		if (userSelection.getControlButton().getMode() == ControlButton.ControlMode.PAUSE) {
			userSelections.add(userSelection);
			userSelection.getTrackPlayer().setTrack(new Track(audioFileList.getAudioFile(userSelection.getNameLabel().getText())));
			userSelection.getTrackPlayer().play(TrackPlayer.PLAY_INDEFINITELY);
			onVolumeSliderChange(userSelection);
		} else {
			userSelections.remove(userSelection);
			userSelection.getTrackPlayer().stop();
			if (userSelections.isEmpty())
				mainUI.getOverlayPanel().getControlPanel().getControlButton().switchMode();
		}
		userSelection.getControlButton().switchMode();
	}
	
	/**
	 * Adjusts volume whenever a volume slider is used.
	 * @param userSelection the user selection containing the volume slider acted on.
	 * @param trackPlayer the <code>TrackPlayer</code> object associated with the given user selection.
	 */
	private void onVolumeSliderChange(UserSelectionPanel userSelection) {
		float masterVolume = mainUI.getOverlayPanel().getControlPanel().getVolumeSlider().getValue() / 100.0f;
		float userSelectionVolume = userSelection.getVolumeSlider().getValue() / 100.0f;
		userSelection.getTrackPlayer().setVolume(userSelectionVolume * masterVolume);
	}
	
	/**
	 * Plays / pauses all current user selections whenever the master control button is used.
	 */
	private void onMasterControlButtonPressed() {
		if (!userSelections.isEmpty()) {
			for (UserSelectionPanel userSelection : userSelections) {
				if (mainUI.getOverlayPanel().getControlPanel().getControlButton().getMode() == ControlButton.ControlMode.PAUSE) {
					userSelection.getTrackPlayer().setTrack(new Track(audioFileList.getAudioFile(userSelection.getNameLabel().getText())));
					userSelection.getTrackPlayer().play(TrackPlayer.PLAY_INDEFINITELY);
					onVolumeSliderChange(userSelection);
				} else
					userSelection.getTrackPlayer().stop();
				userSelection.getControlButton().switchMode();
			}
			mainUI.getOverlayPanel().getControlPanel().getControlButton().switchMode();
		}
	}
	
	/**
	 * Adjusts the master volume whenever the master volume slider is used.
	 */
	private void onMasterVolumeSliderChange() {
		if (!userSelections.isEmpty()) {
			for (UserSelectionPanel userSelection : userSelections) {
				onVolumeSliderChange(userSelection);
			}
		}
	}
}
