/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/

package it.csi.buonodom.buonodombo.external.ateco;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the it.csi.aaep.aaeporch.business package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

	private final static QName _UserException_QNAME = new QName("http://business.aaeporch.aaep.csi.it/",
			"UserException");
	private final static QName _TAVTException_QNAME = new QName("http://business.aaeporch.aaep.csi.it/",
			"TAVTException");
	private final static QName _UnrecoverableException_QNAME = new QName("http://business.aaeporch.aaep.csi.it/",
			"UnrecoverableException");
	private final static QName _SystemException_QNAME = new QName("http://business.aaeporch.aaep.csi.it/",
			"SystemException");
	private final static QName _CSIException_QNAME = new QName("http://business.aaeporch.aaep.csi.it/", "CSIException");
	private final static QName _AAEPException_QNAME = new QName("http://business.aaeporch.aaep.csi.it/",
			"AAEPException");
	private final static QName _GetElencoRegioni_QNAME = new QName("http://business.aaeporch.aaep.csi.it/",
			"getElencoRegioni");
	private final static QName _GetElencoRegioniResponse_QNAME = new QName("http://business.aaeporch.aaep.csi.it/",
			"getElencoRegioniResponse");
	private final static QName _CercaPersoneInfoc_QNAME = new QName("http://business.aaeporch.aaep.csi.it/",
			"cercaPersoneInfoc");
	private final static QName _CercaPersoneInfocResponse_QNAME = new QName("http://business.aaeporch.aaep.csi.it/",
			"cercaPersoneInfocResponse");
	private final static QName _CercaVariazioniAnagrafiche_QNAME = new QName("http://business.aaeporch.aaep.csi.it/",
			"cercaVariazioniAnagrafiche");
	private final static QName _CercaVariazioniAnagraficheResponse_QNAME = new QName(
			"http://business.aaeporch.aaep.csi.it/", "cercaVariazioniAnagraficheResponse");
	private final static QName _GetDettaglioImpresa_QNAME = new QName("http://business.aaeporch.aaep.csi.it/",
			"getDettaglioImpresa");
	private final static QName _GetDettaglioImpresaResponse_QNAME = new QName("http://business.aaeporch.aaep.csi.it/",
			"getDettaglioImpresaResponse");
	private final static QName _GetDettaglioPersonaInfoc_QNAME = new QName("http://business.aaeporch.aaep.csi.it/",
			"getDettaglioPersonaInfoc");
	private final static QName _GetDettaglioPersonaInfocResponse_QNAME = new QName(
			"http://business.aaeporch.aaep.csi.it/", "getDettaglioPersonaInfocResponse");
	private final static QName _GetBilancioAzienda_QNAME = new QName("http://business.aaeporch.aaep.csi.it/",
			"getBilancioAzienda");
	private final static QName _GetBilancioAziendaResponse_QNAME = new QName("http://business.aaeporch.aaep.csi.it/",
			"getBilancioAziendaResponse");
	private final static QName _GetElencoComuniInProvincia_QNAME = new QName("http://business.aaeporch.aaep.csi.it/",
			"getElencoComuniInProvincia");
	private final static QName _GetElencoComuniInProvinciaResponse_QNAME = new QName(
			"http://business.aaeporch.aaep.csi.it/", "getElencoComuniInProvinciaResponse");
	private final static QName _GetDettaglioImpresa2_QNAME = new QName("http://business.aaeporch.aaep.csi.it/",
			"getDettaglioImpresa2");
	private final static QName _GetDettaglioImpresa2Response_QNAME = new QName("http://business.aaeporch.aaep.csi.it/",
			"getDettaglioImpresa2Response");
	private final static QName _GetElencoProvinceInRegione_QNAME = new QName("http://business.aaeporch.aaep.csi.it/",
			"getElencoProvinceInRegione");
	private final static QName _GetElencoProvinceInRegioneResponse_QNAME = new QName(
			"http://business.aaeporch.aaep.csi.it/", "getElencoProvinceInRegioneResponse");
	private final static QName _CercaImprese_QNAME = new QName("http://business.aaeporch.aaep.csi.it/", "cercaImprese");
	private final static QName _CercaImpreseResponse_QNAME = new QName("http://business.aaeporch.aaep.csi.it/",
			"cercaImpreseResponse");
	private final static QName _GetDettaglioSede_QNAME = new QName("http://business.aaeporch.aaep.csi.it/",
			"getDettaglioSede");
	private final static QName _GetDettaglioSedeResponse_QNAME = new QName("http://business.aaeporch.aaep.csi.it/",
			"getDettaglioSedeResponse");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of schema
	 * derived classes for package: it.csi.aaep.aaeporch.business
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link UserException }
	 * 
	 */
	public UserException createUserException() {
		return new UserException();
	}

	/**
	 * Create an instance of {@link TAVTException }
	 * 
	 */
	public TAVTException createTAVTException() {
		return new TAVTException();
	}

	/**
	 * Create an instance of {@link UnrecoverableException }
	 * 
	 */
	public UnrecoverableException createUnrecoverableException() {
		return new UnrecoverableException();
	}

	/**
	 * Create an instance of {@link SystemException }
	 * 
	 */
	public SystemException createSystemException() {
		return new SystemException();
	}

	/**
	 * Create an instance of {@link CSIException }
	 * 
	 */
	public CSIException createCSIException() {
		return new CSIException();
	}

	/**
	 * Create an instance of {@link AAEPException }
	 * 
	 */
	public AAEPException createAAEPException() {
		return new AAEPException();
	}

	/**
	 * Create an instance of {@link GetElencoRegioni }
	 * 
	 */
	public GetElencoRegioni createGetElencoRegioni() {
		return new GetElencoRegioni();
	}

	/**
	 * Create an instance of {@link GetElencoRegioniResponse }
	 * 
	 */
	public GetElencoRegioniResponse createGetElencoRegioniResponse() {
		return new GetElencoRegioniResponse();
	}

	/**
	 * Create an instance of {@link CercaPersoneInfoc }
	 * 
	 */
	public CercaPersoneInfoc createCercaPersoneInfoc() {
		return new CercaPersoneInfoc();
	}

	/**
	 * Create an instance of {@link CercaPersoneInfocResponse }
	 * 
	 */
	public CercaPersoneInfocResponse createCercaPersoneInfocResponse() {
		return new CercaPersoneInfocResponse();
	}

	/**
	 * Create an instance of {@link CercaVariazioniAnagrafiche }
	 * 
	 */
	public CercaVariazioniAnagrafiche createCercaVariazioniAnagrafiche() {
		return new CercaVariazioniAnagrafiche();
	}

	/**
	 * Create an instance of {@link CercaVariazioniAnagraficheResponse }
	 * 
	 */
	public CercaVariazioniAnagraficheResponse createCercaVariazioniAnagraficheResponse() {
		return new CercaVariazioniAnagraficheResponse();
	}

	/**
	 * Create an instance of {@link GetDettaglioImpresa }
	 * 
	 */
	public GetDettaglioImpresa createGetDettaglioImpresa() {
		return new GetDettaglioImpresa();
	}

	/**
	 * Create an instance of {@link GetDettaglioImpresaResponse }
	 * 
	 */
	public GetDettaglioImpresaResponse createGetDettaglioImpresaResponse() {
		return new GetDettaglioImpresaResponse();
	}

	/**
	 * Create an instance of {@link GetDettaglioPersonaInfoc }
	 * 
	 */
	public GetDettaglioPersonaInfoc createGetDettaglioPersonaInfoc() {
		return new GetDettaglioPersonaInfoc();
	}

	/**
	 * Create an instance of {@link GetDettaglioPersonaInfocResponse }
	 * 
	 */
	public GetDettaglioPersonaInfocResponse createGetDettaglioPersonaInfocResponse() {
		return new GetDettaglioPersonaInfocResponse();
	}

	/**
	 * Create an instance of {@link GetBilancioAzienda }
	 * 
	 */
	public GetBilancioAzienda createGetBilancioAzienda() {
		return new GetBilancioAzienda();
	}

	/**
	 * Create an instance of {@link GetBilancioAziendaResponse }
	 * 
	 */
	public GetBilancioAziendaResponse createGetBilancioAziendaResponse() {
		return new GetBilancioAziendaResponse();
	}

	/**
	 * Create an instance of {@link GetElencoComuniInProvincia }
	 * 
	 */
	public GetElencoComuniInProvincia createGetElencoComuniInProvincia() {
		return new GetElencoComuniInProvincia();
	}

	/**
	 * Create an instance of {@link GetElencoComuniInProvinciaResponse }
	 * 
	 */
	public GetElencoComuniInProvinciaResponse createGetElencoComuniInProvinciaResponse() {
		return new GetElencoComuniInProvinciaResponse();
	}

	/**
	 * Create an instance of {@link GetDettaglioImpresa2 }
	 * 
	 */
	public GetDettaglioImpresa2 createGetDettaglioImpresa2() {
		return new GetDettaglioImpresa2();
	}

	/**
	 * Create an instance of {@link GetDettaglioImpresa2Response }
	 * 
	 */
	public GetDettaglioImpresa2Response createGetDettaglioImpresa2Response() {
		return new GetDettaglioImpresa2Response();
	}

	/**
	 * Create an instance of {@link GetElencoProvinceInRegione }
	 * 
	 */
	public GetElencoProvinceInRegione createGetElencoProvinceInRegione() {
		return new GetElencoProvinceInRegione();
	}

	/**
	 * Create an instance of {@link GetElencoProvinceInRegioneResponse }
	 * 
	 */
	public GetElencoProvinceInRegioneResponse createGetElencoProvinceInRegioneResponse() {
		return new GetElencoProvinceInRegioneResponse();
	}

	/**
	 * Create an instance of {@link CercaImprese }
	 * 
	 */
	public CercaImprese createCercaImprese() {
		return new CercaImprese();
	}

	/**
	 * Create an instance of {@link CercaImpreseResponse }
	 * 
	 */
	public CercaImpreseResponse createCercaImpreseResponse() {
		return new CercaImpreseResponse();
	}

	/**
	 * Create an instance of {@link GetDettaglioSede }
	 * 
	 */
	public GetDettaglioSede createGetDettaglioSede() {
		return new GetDettaglioSede();
	}

	/**
	 * Create an instance of {@link GetDettaglioSedeResponse }
	 * 
	 */
	public GetDettaglioSedeResponse createGetDettaglioSedeResponse() {
		return new GetDettaglioSedeResponse();
	}

	/**
	 * Create an instance of {@link Utente }
	 * 
	 */
	public Utente createUtente() {
		return new Utente();
	}

	/**
	 * Create an instance of {@link Regione }
	 * 
	 */
	public Regione createRegione() {
		return new Regione();
	}

	/**
	 * Create an instance of {@link Nazione }
	 * 
	 */
	public Nazione createNazione() {
		return new Nazione();
	}

	/**
	 * Create an instance of {@link Persona }
	 * 
	 */
	public Persona createPersona() {
		return new Persona();
	}

	/**
	 * Create an instance of {@link Carica }
	 * 
	 */
	public Carica createCarica() {
		return new Carica();
	}

	/**
	 * Create an instance of {@link PersonaINFOC }
	 * 
	 */
	public PersonaINFOC createPersonaINFOC() {
		return new PersonaINFOC();
	}

	/**
	 * Create an instance of {@link Fallimento }
	 * 
	 */
	public Fallimento createFallimento() {
		return new Fallimento();
	}

	/**
	 * Create an instance of {@link PersonaINFOCListItem }
	 * 
	 */
	public PersonaINFOCListItem createPersonaINFOCListItem() {
		return new PersonaINFOCListItem();
	}

	/**
	 * Create an instance of {@link ReferenteSILP }
	 * 
	 */
	public ReferenteSILP createReferenteSILP() {
		return new ReferenteSILP();
	}

	/**
	 * Create an instance of {@link Contatti }
	 * 
	 */
	public Contatti createContatti() {
		return new Contatti();
	}

	/**
	 * Create an instance of {@link VariazioneAnagrafica }
	 * 
	 */
	public VariazioneAnagrafica createVariazioneAnagrafica() {
		return new VariazioneAnagrafica();
	}

	/**
	 * Create an instance of {@link DettagliCameraCommercio }
	 * 
	 */
	public DettagliCameraCommercio createDettagliCameraCommercio() {
		return new DettagliCameraCommercio();
	}

	/**
	 * Create an instance of {@link AttivitaEconomica }
	 * 
	 */
	public AttivitaEconomica createAttivitaEconomica() {
		return new AttivitaEconomica();
	}

	/**
	 * Create an instance of {@link DettaglioVariazioneAnagrafica }
	 * 
	 */
	public DettaglioVariazioneAnagrafica createDettaglioVariazioneAnagrafica() {
		return new DettaglioVariazioneAnagrafica();
	}

	/**
	 * Create an instance of {@link Impresa }
	 * 
	 */
	public Impresa createImpresa() {
		return new Impresa();
	}

	/**
	 * Create an instance of {@link Cessazione }
	 * 
	 */
	public Cessazione createCessazione() {
		return new Cessazione();
	}

	/**
	 * Create an instance of {@link DettagliAlboArtigiano }
	 * 
	 */
	public DettagliAlboArtigiano createDettagliAlboArtigiano() {
		return new DettagliAlboArtigiano();
	}

	/**
	 * Create an instance of {@link Sede }
	 * 
	 */
	public Sede createSede() {
		return new Sede();
	}

	/**
	 * Create an instance of {@link Ubicazione }
	 * 
	 */
	public Ubicazione createUbicazione() {
		return new Ubicazione();
	}

	/**
	 * Create an instance of {@link SedeAAEP }
	 * 
	 */
	public SedeAAEP createSedeAAEP() {
		return new SedeAAEP();
	}

	/**
	 * Create an instance of {@link SedeFP }
	 * 
	 */
	public SedeFP createSedeFP() {
		return new SedeFP();
	}

	/**
	 * Create an instance of {@link Corso }
	 * 
	 */
	public Corso createCorso() {
		return new Corso();
	}

	/**
	 * Create an instance of {@link SedeOccasionale }
	 * 
	 */
	public SedeOccasionale createSedeOccasionale() {
		return new SedeOccasionale();
	}

	/**
	 * Create an instance of {@link GenericListItem }
	 * 
	 */
	public GenericListItem createGenericListItem() {
		return new GenericListItem();
	}

	/**
	 * Create an instance of {@link SedeINFOC }
	 * 
	 */
	public SedeINFOC createSedeINFOC() {
		return new SedeINFOC();
	}

	/**
	 * Create an instance of {@link AlboRuolo }
	 * 
	 */
	public AlboRuolo createAlboRuolo() {
		return new AlboRuolo();
	}

	/**
	 * Create an instance of {@link DenunciaAttivita }
	 * 
	 */
	public DenunciaAttivita createDenunciaAttivita() {
		return new DenunciaAttivita();
	}

	/**
	 * Create an instance of {@link SedeFonteINFOC }
	 * 
	 */
	public SedeFonteINFOC createSedeFonteINFOC() {
		return new SedeFonteINFOC();
	}

	/**
	 * Create an instance of {@link SedeListItem }
	 * 
	 */
	public SedeListItem createSedeListItem() {
		return new SedeListItem();
	}

	/**
	 * Create an instance of {@link SedeSILP }
	 * 
	 */
	public SedeSILP createSedeSILP() {
		return new SedeSILP();
	}

	/**
	 * Create an instance of {@link ImpresaINFOC }
	 * 
	 */
	public ImpresaINFOC createImpresaINFOC() {
		return new ImpresaINFOC();
	}

	/**
	 * Create an instance of {@link DatoCostitutivo }
	 * 
	 */
	public DatoCostitutivo createDatoCostitutivo() {
		return new DatoCostitutivo();
	}

	/**
	 * Create an instance of {@link ProcConcors }
	 * 
	 */
	public ProcConcors createProcConcors() {
		return new ProcConcors();
	}

	/**
	 * Create an instance of {@link SezioneSpeciale }
	 * 
	 */
	public SezioneSpeciale createSezioneSpeciale() {
		return new SezioneSpeciale();
	}

	/**
	 * Create an instance of {@link AziendaAAEP }
	 * 
	 */
	public AziendaAAEP createAziendaAAEP() {
		return new AziendaAAEP();
	}

	/**
	 * Create an instance of {@link RappresentanteLegale }
	 * 
	 */
	public RappresentanteLegale createRappresentanteLegale() {
		return new RappresentanteLegale();
	}

	/**
	 * Create an instance of {@link AziendaSILP }
	 * 
	 */
	public AziendaSILP createAziendaSILP() {
		return new AziendaSILP();
	}

	/**
	 * Create an instance of {@link ProspettoInformativoDisabile }
	 * 
	 */
	public ProspettoInformativoDisabile createProspettoInformativoDisabile() {
		return new ProspettoInformativoDisabile();
	}

	/**
	 * Create an instance of {@link OperatoreFormazioneProfessionale }
	 * 
	 */
	public OperatoreFormazioneProfessionale createOperatoreFormazioneProfessionale() {
		return new OperatoreFormazioneProfessionale();
	}

	/**
	 * Create an instance of {@link Bilancio }
	 * 
	 */
	public Bilancio createBilancio() {
		return new Bilancio();
	}

	/**
	 * Create an instance of {@link RigoBilancio }
	 * 
	 */
	public RigoBilancio createRigoBilancio() {
		return new RigoBilancio();
	}

	/**
	 * Create an instance of {@link Comune }
	 * 
	 */
	public Comune createComune() {
		return new Comune();
	}

	/**
	 * Create an instance of {@link Provincia }
	 * 
	 */
	public Provincia createProvincia() {
		return new Provincia();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link UserException
	 * }{@code >}
	 * 
	 * @param value Java instance representing xml element's value.
	 * @return the new instance of {@link JAXBElement }{@code <}{@link UserException
	 *         }{@code >}
	 */
	@XmlElementDecl(namespace = "http://business.aaeporch.aaep.csi.it/", name = "UserException")
	public JAXBElement<UserException> createUserException(UserException value) {
		return new JAXBElement<UserException>(_UserException_QNAME, UserException.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link TAVTException
	 * }{@code >}
	 * 
	 * @param value Java instance representing xml element's value.
	 * @return the new instance of {@link JAXBElement }{@code <}{@link TAVTException
	 *         }{@code >}
	 */
	@XmlElementDecl(namespace = "http://business.aaeporch.aaep.csi.it/", name = "TAVTException")
	public JAXBElement<TAVTException> createTAVTException(TAVTException value) {
		return new JAXBElement<TAVTException>(_TAVTException_QNAME, TAVTException.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link UnrecoverableException }{@code >}
	 * 
	 * @param value Java instance representing xml element's value.
	 * @return the new instance of {@link JAXBElement
	 *         }{@code <}{@link UnrecoverableException }{@code >}
	 */
	@XmlElementDecl(namespace = "http://business.aaeporch.aaep.csi.it/", name = "UnrecoverableException")
	public JAXBElement<UnrecoverableException> createUnrecoverableException(UnrecoverableException value) {
		return new JAXBElement<UnrecoverableException>(_UnrecoverableException_QNAME, UnrecoverableException.class,
				null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link SystemException
	 * }{@code >}
	 * 
	 * @param value Java instance representing xml element's value.
	 * @return the new instance of {@link JAXBElement
	 *         }{@code <}{@link SystemException }{@code >}
	 */
	@XmlElementDecl(namespace = "http://business.aaeporch.aaep.csi.it/", name = "SystemException")
	public JAXBElement<SystemException> createSystemException(SystemException value) {
		return new JAXBElement<SystemException>(_SystemException_QNAME, SystemException.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link CSIException
	 * }{@code >}
	 * 
	 * @param value Java instance representing xml element's value.
	 * @return the new instance of {@link JAXBElement }{@code <}{@link CSIException
	 *         }{@code >}
	 */
	@XmlElementDecl(namespace = "http://business.aaeporch.aaep.csi.it/", name = "CSIException")
	public JAXBElement<CSIException> createCSIException(CSIException value) {
		return new JAXBElement<CSIException>(_CSIException_QNAME, CSIException.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link AAEPException
	 * }{@code >}
	 * 
	 * @param value Java instance representing xml element's value.
	 * @return the new instance of {@link JAXBElement }{@code <}{@link AAEPException
	 *         }{@code >}
	 */
	@XmlElementDecl(namespace = "http://business.aaeporch.aaep.csi.it/", name = "AAEPException")
	public JAXBElement<AAEPException> createAAEPException(AAEPException value) {
		return new JAXBElement<AAEPException>(_AAEPException_QNAME, AAEPException.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link GetElencoRegioni
	 * }{@code >}
	 * 
	 * @param value Java instance representing xml element's value.
	 * @return the new instance of {@link JAXBElement
	 *         }{@code <}{@link GetElencoRegioni }{@code >}
	 */
	@XmlElementDecl(namespace = "http://business.aaeporch.aaep.csi.it/", name = "getElencoRegioni")
	public JAXBElement<GetElencoRegioni> createGetElencoRegioni(GetElencoRegioni value) {
		return new JAXBElement<GetElencoRegioni>(_GetElencoRegioni_QNAME, GetElencoRegioni.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link GetElencoRegioniResponse }{@code >}
	 * 
	 * @param value Java instance representing xml element's value.
	 * @return the new instance of {@link JAXBElement
	 *         }{@code <}{@link GetElencoRegioniResponse }{@code >}
	 */
	@XmlElementDecl(namespace = "http://business.aaeporch.aaep.csi.it/", name = "getElencoRegioniResponse")
	public JAXBElement<GetElencoRegioniResponse> createGetElencoRegioniResponse(GetElencoRegioniResponse value) {
		return new JAXBElement<GetElencoRegioniResponse>(_GetElencoRegioniResponse_QNAME,
				GetElencoRegioniResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link CercaPersoneInfoc
	 * }{@code >}
	 * 
	 * @param value Java instance representing xml element's value.
	 * @return the new instance of {@link JAXBElement
	 *         }{@code <}{@link CercaPersoneInfoc }{@code >}
	 */
	@XmlElementDecl(namespace = "http://business.aaeporch.aaep.csi.it/", name = "cercaPersoneInfoc")
	public JAXBElement<CercaPersoneInfoc> createCercaPersoneInfoc(CercaPersoneInfoc value) {
		return new JAXBElement<CercaPersoneInfoc>(_CercaPersoneInfoc_QNAME, CercaPersoneInfoc.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link CercaPersoneInfocResponse }{@code >}
	 * 
	 * @param value Java instance representing xml element's value.
	 * @return the new instance of {@link JAXBElement
	 *         }{@code <}{@link CercaPersoneInfocResponse }{@code >}
	 */
	@XmlElementDecl(namespace = "http://business.aaeporch.aaep.csi.it/", name = "cercaPersoneInfocResponse")
	public JAXBElement<CercaPersoneInfocResponse> createCercaPersoneInfocResponse(CercaPersoneInfocResponse value) {
		return new JAXBElement<CercaPersoneInfocResponse>(_CercaPersoneInfocResponse_QNAME,
				CercaPersoneInfocResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link CercaVariazioniAnagrafiche }{@code >}
	 * 
	 * @param value Java instance representing xml element's value.
	 * @return the new instance of {@link JAXBElement
	 *         }{@code <}{@link CercaVariazioniAnagrafiche }{@code >}
	 */
	@XmlElementDecl(namespace = "http://business.aaeporch.aaep.csi.it/", name = "cercaVariazioniAnagrafiche")
	public JAXBElement<CercaVariazioniAnagrafiche> createCercaVariazioniAnagrafiche(CercaVariazioniAnagrafiche value) {
		return new JAXBElement<CercaVariazioniAnagrafiche>(_CercaVariazioniAnagrafiche_QNAME,
				CercaVariazioniAnagrafiche.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link CercaVariazioniAnagraficheResponse }{@code >}
	 * 
	 * @param value Java instance representing xml element's value.
	 * @return the new instance of {@link JAXBElement
	 *         }{@code <}{@link CercaVariazioniAnagraficheResponse }{@code >}
	 */
	@XmlElementDecl(namespace = "http://business.aaeporch.aaep.csi.it/", name = "cercaVariazioniAnagraficheResponse")
	public JAXBElement<CercaVariazioniAnagraficheResponse> createCercaVariazioniAnagraficheResponse(
			CercaVariazioniAnagraficheResponse value) {
		return new JAXBElement<CercaVariazioniAnagraficheResponse>(_CercaVariazioniAnagraficheResponse_QNAME,
				CercaVariazioniAnagraficheResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link GetDettaglioImpresa
	 * }{@code >}
	 * 
	 * @param value Java instance representing xml element's value.
	 * @return the new instance of {@link JAXBElement
	 *         }{@code <}{@link GetDettaglioImpresa }{@code >}
	 */
	@XmlElementDecl(namespace = "http://business.aaeporch.aaep.csi.it/", name = "getDettaglioImpresa")
	public JAXBElement<GetDettaglioImpresa> createGetDettaglioImpresa(GetDettaglioImpresa value) {
		return new JAXBElement<GetDettaglioImpresa>(_GetDettaglioImpresa_QNAME, GetDettaglioImpresa.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link GetDettaglioImpresaResponse }{@code >}
	 * 
	 * @param value Java instance representing xml element's value.
	 * @return the new instance of {@link JAXBElement
	 *         }{@code <}{@link GetDettaglioImpresaResponse }{@code >}
	 */
	@XmlElementDecl(namespace = "http://business.aaeporch.aaep.csi.it/", name = "getDettaglioImpresaResponse")
	public JAXBElement<GetDettaglioImpresaResponse> createGetDettaglioImpresaResponse(
			GetDettaglioImpresaResponse value) {
		return new JAXBElement<GetDettaglioImpresaResponse>(_GetDettaglioImpresaResponse_QNAME,
				GetDettaglioImpresaResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link GetDettaglioPersonaInfoc }{@code >}
	 * 
	 * @param value Java instance representing xml element's value.
	 * @return the new instance of {@link JAXBElement
	 *         }{@code <}{@link GetDettaglioPersonaInfoc }{@code >}
	 */
	@XmlElementDecl(namespace = "http://business.aaeporch.aaep.csi.it/", name = "getDettaglioPersonaInfoc")
	public JAXBElement<GetDettaglioPersonaInfoc> createGetDettaglioPersonaInfoc(GetDettaglioPersonaInfoc value) {
		return new JAXBElement<GetDettaglioPersonaInfoc>(_GetDettaglioPersonaInfoc_QNAME,
				GetDettaglioPersonaInfoc.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link GetDettaglioPersonaInfocResponse }{@code >}
	 * 
	 * @param value Java instance representing xml element's value.
	 * @return the new instance of {@link JAXBElement
	 *         }{@code <}{@link GetDettaglioPersonaInfocResponse }{@code >}
	 */
	@XmlElementDecl(namespace = "http://business.aaeporch.aaep.csi.it/", name = "getDettaglioPersonaInfocResponse")
	public JAXBElement<GetDettaglioPersonaInfocResponse> createGetDettaglioPersonaInfocResponse(
			GetDettaglioPersonaInfocResponse value) {
		return new JAXBElement<GetDettaglioPersonaInfocResponse>(_GetDettaglioPersonaInfocResponse_QNAME,
				GetDettaglioPersonaInfocResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link GetBilancioAzienda
	 * }{@code >}
	 * 
	 * @param value Java instance representing xml element's value.
	 * @return the new instance of {@link JAXBElement
	 *         }{@code <}{@link GetBilancioAzienda }{@code >}
	 */
	@XmlElementDecl(namespace = "http://business.aaeporch.aaep.csi.it/", name = "getBilancioAzienda")
	public JAXBElement<GetBilancioAzienda> createGetBilancioAzienda(GetBilancioAzienda value) {
		return new JAXBElement<GetBilancioAzienda>(_GetBilancioAzienda_QNAME, GetBilancioAzienda.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link GetBilancioAziendaResponse }{@code >}
	 * 
	 * @param value Java instance representing xml element's value.
	 * @return the new instance of {@link JAXBElement
	 *         }{@code <}{@link GetBilancioAziendaResponse }{@code >}
	 */
	@XmlElementDecl(namespace = "http://business.aaeporch.aaep.csi.it/", name = "getBilancioAziendaResponse")
	public JAXBElement<GetBilancioAziendaResponse> createGetBilancioAziendaResponse(GetBilancioAziendaResponse value) {
		return new JAXBElement<GetBilancioAziendaResponse>(_GetBilancioAziendaResponse_QNAME,
				GetBilancioAziendaResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link GetElencoComuniInProvincia }{@code >}
	 * 
	 * @param value Java instance representing xml element's value.
	 * @return the new instance of {@link JAXBElement
	 *         }{@code <}{@link GetElencoComuniInProvincia }{@code >}
	 */
	@XmlElementDecl(namespace = "http://business.aaeporch.aaep.csi.it/", name = "getElencoComuniInProvincia")
	public JAXBElement<GetElencoComuniInProvincia> createGetElencoComuniInProvincia(GetElencoComuniInProvincia value) {
		return new JAXBElement<GetElencoComuniInProvincia>(_GetElencoComuniInProvincia_QNAME,
				GetElencoComuniInProvincia.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link GetElencoComuniInProvinciaResponse }{@code >}
	 * 
	 * @param value Java instance representing xml element's value.
	 * @return the new instance of {@link JAXBElement
	 *         }{@code <}{@link GetElencoComuniInProvinciaResponse }{@code >}
	 */
	@XmlElementDecl(namespace = "http://business.aaeporch.aaep.csi.it/", name = "getElencoComuniInProvinciaResponse")
	public JAXBElement<GetElencoComuniInProvinciaResponse> createGetElencoComuniInProvinciaResponse(
			GetElencoComuniInProvinciaResponse value) {
		return new JAXBElement<GetElencoComuniInProvinciaResponse>(_GetElencoComuniInProvinciaResponse_QNAME,
				GetElencoComuniInProvinciaResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link GetDettaglioImpresa2 }{@code >}
	 * 
	 * @param value Java instance representing xml element's value.
	 * @return the new instance of {@link JAXBElement
	 *         }{@code <}{@link GetDettaglioImpresa2 }{@code >}
	 */
	@XmlElementDecl(namespace = "http://business.aaeporch.aaep.csi.it/", name = "getDettaglioImpresa2")
	public JAXBElement<GetDettaglioImpresa2> createGetDettaglioImpresa2(GetDettaglioImpresa2 value) {
		return new JAXBElement<GetDettaglioImpresa2>(_GetDettaglioImpresa2_QNAME, GetDettaglioImpresa2.class, null,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link GetDettaglioImpresa2Response }{@code >}
	 * 
	 * @param value Java instance representing xml element's value.
	 * @return the new instance of {@link JAXBElement
	 *         }{@code <}{@link GetDettaglioImpresa2Response }{@code >}
	 */
	@XmlElementDecl(namespace = "http://business.aaeporch.aaep.csi.it/", name = "getDettaglioImpresa2Response")
	public JAXBElement<GetDettaglioImpresa2Response> createGetDettaglioImpresa2Response(
			GetDettaglioImpresa2Response value) {
		return new JAXBElement<GetDettaglioImpresa2Response>(_GetDettaglioImpresa2Response_QNAME,
				GetDettaglioImpresa2Response.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link GetElencoProvinceInRegione }{@code >}
	 * 
	 * @param value Java instance representing xml element's value.
	 * @return the new instance of {@link JAXBElement
	 *         }{@code <}{@link GetElencoProvinceInRegione }{@code >}
	 */
	@XmlElementDecl(namespace = "http://business.aaeporch.aaep.csi.it/", name = "getElencoProvinceInRegione")
	public JAXBElement<GetElencoProvinceInRegione> createGetElencoProvinceInRegione(GetElencoProvinceInRegione value) {
		return new JAXBElement<GetElencoProvinceInRegione>(_GetElencoProvinceInRegione_QNAME,
				GetElencoProvinceInRegione.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link GetElencoProvinceInRegioneResponse }{@code >}
	 * 
	 * @param value Java instance representing xml element's value.
	 * @return the new instance of {@link JAXBElement
	 *         }{@code <}{@link GetElencoProvinceInRegioneResponse }{@code >}
	 */
	@XmlElementDecl(namespace = "http://business.aaeporch.aaep.csi.it/", name = "getElencoProvinceInRegioneResponse")
	public JAXBElement<GetElencoProvinceInRegioneResponse> createGetElencoProvinceInRegioneResponse(
			GetElencoProvinceInRegioneResponse value) {
		return new JAXBElement<GetElencoProvinceInRegioneResponse>(_GetElencoProvinceInRegioneResponse_QNAME,
				GetElencoProvinceInRegioneResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link CercaImprese
	 * }{@code >}
	 * 
	 * @param value Java instance representing xml element's value.
	 * @return the new instance of {@link JAXBElement }{@code <}{@link CercaImprese
	 *         }{@code >}
	 */
	@XmlElementDecl(namespace = "http://business.aaeporch.aaep.csi.it/", name = "cercaImprese")
	public JAXBElement<CercaImprese> createCercaImprese(CercaImprese value) {
		return new JAXBElement<CercaImprese>(_CercaImprese_QNAME, CercaImprese.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link CercaImpreseResponse }{@code >}
	 * 
	 * @param value Java instance representing xml element's value.
	 * @return the new instance of {@link JAXBElement
	 *         }{@code <}{@link CercaImpreseResponse }{@code >}
	 */
	@XmlElementDecl(namespace = "http://business.aaeporch.aaep.csi.it/", name = "cercaImpreseResponse")
	public JAXBElement<CercaImpreseResponse> createCercaImpreseResponse(CercaImpreseResponse value) {
		return new JAXBElement<CercaImpreseResponse>(_CercaImpreseResponse_QNAME, CercaImpreseResponse.class, null,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link GetDettaglioSede
	 * }{@code >}
	 * 
	 * @param value Java instance representing xml element's value.
	 * @return the new instance of {@link JAXBElement
	 *         }{@code <}{@link GetDettaglioSede }{@code >}
	 */
	@XmlElementDecl(namespace = "http://business.aaeporch.aaep.csi.it/", name = "getDettaglioSede")
	public JAXBElement<GetDettaglioSede> createGetDettaglioSede(GetDettaglioSede value) {
		return new JAXBElement<GetDettaglioSede>(_GetDettaglioSede_QNAME, GetDettaglioSede.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement
	 * }{@code <}{@link GetDettaglioSedeResponse }{@code >}
	 * 
	 * @param value Java instance representing xml element's value.
	 * @return the new instance of {@link JAXBElement
	 *         }{@code <}{@link GetDettaglioSedeResponse }{@code >}
	 */
	@XmlElementDecl(namespace = "http://business.aaeporch.aaep.csi.it/", name = "getDettaglioSedeResponse")
	public JAXBElement<GetDettaglioSedeResponse> createGetDettaglioSedeResponse(GetDettaglioSedeResponse value) {
		return new JAXBElement<GetDettaglioSedeResponse>(_GetDettaglioSedeResponse_QNAME,
				GetDettaglioSedeResponse.class, null, value);
	}

}
