\* = bug fix  
\+ = added feature  
~ = code clean up  

PassManager-B2:  
*MainScreen no longer disposes and reopens on refresh (due to bug where screen would sometimes not dispose and 2 main screens would exist simultaneously)

*removed string comparison using == instead of .equals() (MenuOptionEditor.java:211)

*removed label focusing in the menu option editor

~combined private classes contained in the same class (MenuOptionEditor, NumPad)

~removed unneeded BorderLayout references and import from MainScreen

*placed container JPanel between JFrame and other components on MainScreen (all components -> JPanel "container" -> JFrame "frame") (to facilitate MainScreen bug fix)

~removed unused imports from PasswordScreen

+finished unimplemented KeyListener in PasswordScreen
