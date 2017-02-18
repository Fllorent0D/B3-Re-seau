/* PreferencesWeb.java */
package Preferences;
import java.beans.*; 
import java.io.Serializable; 
import java.awt.*;

public class PreferencesWeb extends Object implements Serializable {
	private PropertyChangeSupport propertySupport;
	public PreferencesWeb() {
		couleurFond = "blue"; couleurTrait = "black";
		langue = "FR"; uniteMonetaire = "EUR"; propertySupport = new PropertyChangeSupport(this);}
	public void addPropertyChangeListener(PropertyChangeListener listener){ 
		propertySupport.addPropertyChangeListener(listener); }
	public void removePropertyChangeListener(PropertyChangeListener listener) { 
		propertySupport.removePropertyChangeListener(listener); }
	private String couleurFond; public String getCouleurFond() {
		return this.couleurFond; }
	public void setCouleurFond(String couleurFond) {
		String oldCouleurFond = this.couleurFond;
		this.couleurFond = couleurFond;
		propertySupport.firePropertyChange ("couleurFond", oldCouleurFond, couleurFond);}

	private String couleurTrait;
	public String getCouleurTrait(){ 
		return this.couleurTrait; }
	public void setCouleurTrait(String couleurTrait) { 
		this.couleurTrait = couleurTrait; }

	private String langue;
	public String getLangue(){ 
		return this.langue; }
	public void setLangue(String langue) { 
		this.langue = langue; }
	private String uniteMonetaire;
	public String getUniteMonetaire(){ 
		return this.uniteMonetaire; }
	public void setUniteMonetaire(String uniteMonetaire) {
		String oldUniteMonetaire = this.uniteMonetaire;
		this.uniteMonetaire = uniteMonetaire;
		propertySupport.firePropertyChange ("uniteMonetaire", oldUniteMonetaire,
		uniteMonetaire); }
}