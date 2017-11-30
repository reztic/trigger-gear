package edu.cs151.trigger;

import java.awt.Rectangle;
import java.awt.font.TextHitInfo;
import java.awt.im.InputMethodRequests;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import javax.swing.JPanel;

public class GameJPanel extends JPanel implements InputMethodRequests {

    private final InputMethodRequests inputMethodRequests;

    public GameJPanel() {
        if (System.getProperty("os.name").toLowerCase().startsWith("mac")) {
        	System.out.println("Fucking mac");
            // macOS Sierra prevents key input after holding certain keys (like 'e' or 'n')
            // because of a native input method popup that isn't working with Java correctly.
            //
            // This will still fail is some scenarios, like when pressing a digit after
            // holding 'e'. The digit will not have a keypress event.
            //
            // Tested on macOS 10.12.0 and Java 8 versions b76 - b101.
            // (This may also be happening on older versions of macOS if the
            // ApplePressAndHoldEnabled option is set to true - untested at the moment.)
            //
            // This workaround keeps key input working by allowing the popup to appear,
            // but places it offscreen.
            //
            // Note, input methods are enabled by default.
            inputMethodRequests = this;
        } else {
            // On all other operating systems, disable input methods normally.
            inputMethodRequests = null;
            enableInputMethods(false);
        }
    }

    @Override
    public InputMethodRequests getInputMethodRequests() {
        return inputMethodRequests;
    }

    // MARK: InputMethodRequests implementation

    @Override
    public Rectangle getTextLocation(TextHitInfo textHitInfo) {
        // In screen coordinates, not window coordinates.
        return new Rectangle(-32768, -32768, 0, 0);
    }

    @Override
    public TextHitInfo getLocationOffset(int x, int y) {
        return null;
    }

    @Override
    public int getInsertPositionOffset() {
        return 0;
    }

    @Override
    public AttributedCharacterIterator getCommittedText(int beginIndex, int endIndex,
                                                        AttributedCharacterIterator.Attribute[] attributes) {
        return null;
    }

    @Override
    public int getCommittedTextLength() {
        return 0;
    }

    @Override
    public AttributedCharacterIterator cancelLatestCommittedText(AttributedCharacterIterator.Attribute[] attributes) {
        return null;
    }

    private static final AttributedCharacterIterator EMPTY_TEXT =
            (new AttributedString("")).getIterator();

    @Override
    public AttributedCharacterIterator getSelectedText(AttributedCharacterIterator.Attribute[] attributes) {
        return EMPTY_TEXT;
    }
}
