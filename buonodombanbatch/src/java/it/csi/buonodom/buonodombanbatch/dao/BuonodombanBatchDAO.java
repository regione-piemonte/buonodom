/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombanbatch.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import it.csi.buonodom.buonodombanbatch.configuration.Configuration;
import it.csi.buonodom.buonodombanbatch.dto.RichiestaDto;
import it.csi.buonodom.buonodombanbatch.logger.BatchLoggerFactory;
import it.csi.buonodom.buonodombanbatch.util.Checker;
import it.csi.buonodom.buonodombanbatch.util.Constants;

public class BuonodombanBatchDAO {

	private Connection conn;
	private PreparedStatement ps;
	private String keyEncrypt = null;

	public BuonodombanBatchDAO(Connection conn) {
		this.conn = conn;
		keyEncrypt = Configuration.get("keyEncrypt");
	}

	public void commit() {
		try {
			this.conn.commit();
		} catch (SQLException e) {
			BatchLoggerFactory.getLogger(this.getClass()).error("ERROR WHILE COMMITTING: ", e);
		}
	}

	public void rollback() {
		try {
			this.conn.rollback();
		} catch (SQLException e1) {
			BatchLoggerFactory.getLogger(this.getClass()).error("ERROR WHILE ROLLBACKING: ", e1);
		}
	}

	public void closeAll() {
		try {
			if (this.ps != null)
				ps.close();

			if (this.conn != null)
				this.conn.close();

		} catch (SQLException e) {
			BatchLoggerFactory.getLogger(this.getClass()).error("ERROR WHILE CLOSING CONNECTION: ", e);
		}
	}

	public List<RichiestaDto> getRichieste() throws SQLException {
		try {
			this.ps = this.conn.prepareStatement(SQLStatements.GETRICHIESTE);
			ResultSet rs = this.ps.executeQuery();
			List<RichiestaDto> listaric = new ArrayList<RichiestaDto>();
			while (rs.next()) {
				RichiestaDto richiesta = new RichiestaDto();
				richiesta.setCf(rs.getString(1));
				richiesta.setDomandaDetId(rs.getLong(2));
				richiesta.setStato(rs.getString(3));
				richiesta.setInizioValidita(rs.getDate(4));
				richiesta.setGiorni(rs.getInt(5));
				richiesta.setRichiedenteCf(rs.getString(6));
				richiesta.setDomandaNumero(rs.getString(7));
				richiesta.setSportelloId(rs.getLong(8));
				richiesta.setDomandaId(rs.getLong(9));
				richiesta.setIban(rs.getString(10));
				richiesta.setIbanIntestatario(rs.getString(11));
				listaric.add(richiesta);
			}
			return listaric;
		} catch (SQLException sqEx) {
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. Get Richieste. Elaborazione Batch terminata con errori =", sqEx);
			throw new SQLException();
		} finally {
			this.ps.close();
		}
	}

	public void insertAudit(String request_payload, String response_payload, Integer esito_chiamata)
			throws SQLException {
		try {
			this.ps = this.conn.prepareStatement(SQLStatements.INSERT_LOG_AUDIT);
			this.ps.setString(1, request_payload);
			this.ps.setString(2, keyEncrypt);
			this.ps.setString(3, response_payload);
			this.ps.setString(4, keyEncrypt);
			this.ps.setInt(5, esito_chiamata);

			this.ps.executeUpdate();
			commit();

		} catch (SQLException e) {
			BatchLoggerFactory.getLogger(this)
					.error("Si e' verificato un errore SQL. insertAudit. Elaborazione Batch terminata con errori =", e);
			this.conn.rollback();
			throw e;
		} finally {
			try {
				this.ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public Boolean checkStatoGraduatoria(Long sportelloId) throws SQLException {

		try {
			this.ps = this.conn.prepareStatement(SQLStatements.CHECK_STATO_GRADUATORIA);
			this.ps.setLong(1, sportelloId);

			ResultSet rs = this.ps.executeQuery();
			while (rs.next()) {
				return rs.getBoolean(1);
			}
		} catch (SQLException sqEx) {
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. Ckeck stato graduatoria. Elaborazione Batch terminata con errori =",
					sqEx);
			throw new SQLException();
		} finally {
			this.ps.close();
		}
		return false;
	}

	public String getCodGraduatoria(Long sportelloId) throws SQLException {
		try {
			this.ps = this.conn.prepareStatement(SQLStatements.DESC_GRADUATORIA_PUBBLICATA);
			this.ps.setLong(1, sportelloId);
			ResultSet rs = this.ps.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException sqEx) {
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. Get desc graduatoria Errore. Elaborazione Batch terminata con errori =",
					sqEx);
			throw new SQLException();
		} finally {
			this.ps.close();
		}
		return null;
	}

	public String getCodGraduatoriaUltima() throws SQLException {
		try {
			this.ps = this.conn.prepareStatement(SQLStatements.DESC_GRADUATORIA_ULTIMA_PUBBLICATA);
			ResultSet rs = this.ps.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException sqEx) {
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. Get desc graduatoria ultima pubblicata Errore. Elaborazione Batch terminata con errori =",
					sqEx);
			throw new SQLException();
		} finally {
			this.ps.close();
		}
		return null;
	}

	public List<RichiestaDto> getRichiesteNonFinanziate() throws SQLException {
		try {
			this.ps = this.conn.prepareStatement(SQLStatements.GETRICHIESTENONFINANZIATE);
			ResultSet rs = this.ps.executeQuery();
			List<RichiestaDto> listaric = new ArrayList<RichiestaDto>();
			while (rs.next()) {
				RichiestaDto richiesta = new RichiestaDto();
				richiesta.setCf(rs.getString(1));
				richiesta.setDomandaDetId(rs.getLong(2));
				richiesta.setStato(rs.getString(3));
				richiesta.setInizioValidita(rs.getDate(4));
				richiesta.setGiorni(rs.getInt(5));
				richiesta.setRichiedenteCf(rs.getString(6));
				richiesta.setDomandaNumero(rs.getString(7));
				richiesta.setSportelloId(rs.getLong(8));
				richiesta.setDomandaId(rs.getLong(9));
				listaric.add(richiesta);
			}
			return listaric;
		} catch (SQLException sqEx) {
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. Get Richieste non finanziate. Elaborazione Batch terminata con errori =",
					sqEx);
			throw new SQLException();
		} finally {
			this.ps.close();
		}
	}

	public String selectStatoPrecedente(String numeroRichiesta) throws SQLException {
		try {
			this.ps = this.conn.prepareStatement(SQLStatements.SELECT_PRECEDENTE_STATO);
			this.ps.setString(1, numeroRichiesta);
			ResultSet rs = this.ps.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException sqEx) {
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. Get stato precedente Errore. Elaborazione Batch terminata con errori =",
					sqEx);
			throw new SQLException();
		} finally {
			this.ps.close();
		}
		return null;
	}

	public long selectEsisteBuonoRendicontazione(Long idDomanda, Long idSportello) throws SQLException {
		try {
			this.ps = this.conn.prepareStatement(SQLStatements.SELECT_BUONO_CREATO);
			this.ps.setLong(1, idDomanda);
			this.ps.setLong(2, idSportello);
			ResultSet rs = this.ps.executeQuery();
			if (rs.next()) {
				return rs.getLong(1);
			}
		} catch (SQLException sqEx) {
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. Get buono rendicontazione. Elaborazione Batch terminata con errori =",
					sqEx);
			throw new SQLException();
		} finally {
			this.ps.close();
		}
		return 0;
	}

	public long insertBuono(Long domandaDetId, Long sportelloId, Long domandaId, String iban, String ibanIntestatario)
			throws SQLException {
		long idnew = 0;
		ResultSet rs = null;
		BatchLoggerFactory.getLogger(this).info("--- INSERISCO BUONO DOMANDA --- " + domandaId);
		try {
			this.ps = this.conn.prepareStatement(SQLStatements.INSERT_INTO_BUONO, Statement.RETURN_GENERATED_KEYS);
			this.ps.setLong(1, domandaDetId);
			this.ps.setLong(2, sportelloId);
			this.ps.setLong(3, domandaId);
			int num = this.ps.executeUpdate();
			if (num > 0) {
				rs = ps.getGeneratedKeys();
				if (rs != null && rs.next()) {
					idnew = rs.getLong(1);
					// genero lo stato
					insertStatoBuono(idnew, Constants.CREATO, iban, ibanIntestatario);
					if (Checker.isValorizzato(idnew)) {
						insertFornitoreBuono(idnew, domandaDetId);
					}
				}
				commit();
				BatchLoggerFactory.getLogger(this).info("--- FINE INSERISCO BUONO --- " + idnew);
				return idnew;
			}
		} catch (SQLException e) {
			String methodName = "INSERT_INTO_BUONO";
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. " + methodName + ". Elaborazione Batch terminata con errori =", e);
			rollback();
			throw new SQLException(e);
		} finally {
			this.ps.close();
		}
		return idnew;
	}

	public void insertStatoBuono(Long buonoId, String buonoStatoCod, String iban, String ibanIntestatario)
			throws SQLException {
		BatchLoggerFactory.getLogger(this).info("--- INSERISCO STATO BUONO --- " + buonoId);
		try {
			this.ps = this.conn.prepareStatement(SQLStatements.INSERT_INTO_BUONO_STATO);
			this.ps.setLong(1, buonoId);
			this.ps.setString(2, buonoStatoCod);
			this.ps.setString(3, iban);
			this.ps.setString(4, ibanIntestatario);
			this.ps.executeUpdate();
			BatchLoggerFactory.getLogger(this).info("--- FINE INSERISCO STATO BUONO --- " + buonoId);
		} catch (SQLException e) {
			String methodName = "INSERT_INTO_BUONO_STATO";
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. " + methodName + ". Elaborazione Batch terminata con errori =", e);
			rollback();
			throw new SQLException(e);
		} finally {
			this.ps.close();
		}
	}

	public long insertFornitoreBuono(Long buonoId, Long domandaDetId) throws SQLException {
		long idnew = 0;
		ResultSet rs = null;
		BatchLoggerFactory.getLogger(this).info("--- INSERISCO FORNITORE BUONO --- " + buonoId);
		try {
			this.ps = this.conn.prepareStatement(SQLStatements.INSERT_INTO_BUONO_FORNITORE,
					Statement.RETURN_GENERATED_KEYS);
			this.ps.setLong(1, buonoId);
			this.ps.setLong(2, domandaDetId);
			int num = this.ps.executeUpdate();
			if (num > 0) {
				rs = ps.getGeneratedKeys();
				if (rs != null && rs.next()) {
					idnew = rs.getLong(1);
					BatchLoggerFactory.getLogger(this).info("--- FINE INSERISCO FORNITORE BUONO --- " + idnew);
					// genero lo stato
					Long idContratto = insertContrattoBuono(buonoId, idnew, domandaDetId);
				}
				return idnew;
			}
		} catch (SQLException e) {
			String methodName = "INSERT_INTO_BUONO_FORNITORE";
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. " + methodName + ". Elaborazione Batch terminata con errori =", e);
			rollback();
			throw new SQLException(e);
		} finally {
			this.ps.close();
		}
		return idnew;
	}

	public long insertContrattoBuono(Long buonoId, Long fornitoreId, Long domandaDetId) throws SQLException {
		long idnew = 0;
		ResultSet rs = null;
		BatchLoggerFactory.getLogger(this).info("--- INSERISCO CONTRATTO BUONO --- " + buonoId);
		try {
			this.ps = this.conn.prepareStatement(SQLStatements.INSERT_INTO_BUONO_CONTRATTO,
					Statement.RETURN_GENERATED_KEYS);
			this.ps.setLong(1, domandaDetId);
			this.ps.setLong(2, buonoId);
			this.ps.setLong(3, fornitoreId);
			this.ps.setLong(4, domandaDetId);
			int num = this.ps.executeUpdate();
			if (num > 0) {
				rs = ps.getGeneratedKeys();
				if (rs != null && rs.next()) {
					idnew = rs.getLong(1);
					BatchLoggerFactory.getLogger(this).info("--- FINE INSERISCO CONTRATTO BUONO --- " + idnew);
					Long allegatoId = insertAllegatoBuono(domandaDetId);
					if (allegatoId != 0)
						insertAllegatoContrattoBuono(idnew, allegatoId);
				}
			}
		} catch (SQLException e) {
			String methodName = "INSERT_INTO_BUONO_CONTRATTO";
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. " + methodName + ". Elaborazione Batch terminata con errori =", e);
			rollback();
			throw new SQLException(e);
		} finally {
			this.ps.close();
		}
		return idnew;
	}

	public long insertAllegatoBuono(Long domandaDetId) throws SQLException {
		long idnew = 0;
		ResultSet rs = null;
		BatchLoggerFactory.getLogger(this).info("--- INSERISCO ALLEGATO BUONO --- ");
		try {
			this.ps = this.conn.prepareStatement(SQLStatements.INSERT_INTO_BUONO_ALLEGATO,
					Statement.RETURN_GENERATED_KEYS);
			this.ps.setLong(1, domandaDetId);
			int num = this.ps.executeUpdate();
			if (num > 0) {
				rs = ps.getGeneratedKeys();
				if (rs != null && rs.next()) {
					idnew = rs.getLong(1);
					BatchLoggerFactory.getLogger(this).info("--- FINE INSERISCO ALLEGATO BUONO --- " + idnew);
				}
			}
		} catch (SQLException e) {
			String methodName = "INSERT_INTO_BUONO_ALLEGATO";
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. " + methodName + ". Elaborazione Batch terminata con errori =", e);
			rollback();
			throw new SQLException(e);
		} finally {
			this.ps.close();
		}
		return idnew;
	}

	public void insertAllegatoContrattoBuono(Long contrattoId, Long allegatoId) throws SQLException {
		BatchLoggerFactory.getLogger(this).info(
				"--- INSERISCO CONTRATTO ALLEGATO BUONO --- " + "CONTRTATTO " + contrattoId + "ALLEGATO " + allegatoId);
		try {
			this.ps = this.conn.prepareStatement(SQLStatements.INSERT_INTO_R_CONTRATTO_ALLEGATO);
			this.ps.setLong(1, contrattoId);
			this.ps.setLong(2, allegatoId);
			this.ps.executeUpdate();
			BatchLoggerFactory.getLogger(this).info("--- FINE INSERISCO CONTRATTO ALLEGATO BUONO --- " + "CONTRTATTO "
					+ contrattoId + "ALLEGATO " + allegatoId);
		} catch (SQLException e) {
			String methodName = "INSERT_INTO_R_CONTRATTO_ALLEGATO";
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. " + methodName + ". Elaborazione Batch terminata con errori =", e);
			rollback();
			throw new SQLException(e);
		} finally {
			this.ps.close();
		}
	}

	public String getParametro(String codice, String tipo) throws SQLException {
		try {
			this.ps = this.conn.prepareStatement(SQLStatements.SELECT_PARAMETRO);
			this.ps.setString(1, codice);
			this.ps.setString(2, tipo);
			ResultSet rs = this.ps.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException sqEx) {
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. Get Errore. Elaborazione Batch terminata con errori =", sqEx);
			throw new SQLException();
		} finally {
			this.ps.close();
		}
		return null;
	}

	public List<RichiestaDto> getDomandeInvioRendicontazioneBandi(String codStato) throws SQLException {
		try {
			this.ps = this.conn.prepareStatement(SQLStatements.GET_DOMANDE_INVIO_DICHIARAZIONE_BANDI);
			this.ps.setString(1, codStato);
			ResultSet rs = this.ps.executeQuery();
			List<RichiestaDto> listaric = new ArrayList<RichiestaDto>();
			while (rs.next()) {
				RichiestaDto richiesta = new RichiestaDto();
				richiesta.setCf(rs.getString(1));
				richiesta.setDomandaDetId(rs.getLong(2));
				richiesta.setStato(rs.getString(3));
				richiesta.setInizioValidita(rs.getDate(4));
				richiesta.setGiorni(rs.getInt(5));
				richiesta.setRichiedenteCf(rs.getString(6));
				richiesta.setDomandaNumero(rs.getString(7));
				richiesta.setSportelloId(rs.getLong(8));
				richiesta.setDomandaId(rs.getLong(9));
				listaric.add(richiesta);
			}
			return listaric;
		} catch (SQLException sqEx) {
			BatchLoggerFactory.getLogger(this).error(
					"Si e' verificato un errore SQL. Get domande invio rendicontazione bandi. Elaborazione Batch terminata con errori =",
					sqEx);
			throw new SQLException();
		} finally {
			this.ps.close();
		}
	}
}
