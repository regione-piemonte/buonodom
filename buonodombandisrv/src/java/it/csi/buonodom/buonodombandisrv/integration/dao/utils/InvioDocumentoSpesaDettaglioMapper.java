/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombandisrv.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonodom.buonodombandisrv.dto.ModelInvioDocumentoSpesa;

public class InvioDocumentoSpesaDettaglioMapper implements RowMapper<ModelInvioDocumentoSpesa> {

	@Override
	public ModelInvioDocumentoSpesa mapRow(ResultSet rs, int rowNum) throws SQLException {

		ModelInvioDocumentoSpesa dettaglio = new ModelInvioDocumentoSpesa();
		dettaglio.setCodiceVoceDiSpesa("BD1");
		dettaglio.setImportoQuietanziato(rs.getString("importo_totale_pagato"));
		dettaglio.setImportoTotaleDocumento(rs.getString("importo_quietanzato"));
		dettaglio.setNumeroDocumento(rs.getString("doc_numero"));
		dettaglio.setTipologiaDocumento(rs.getString("doc_spesa_tipo_cod"));
		dettaglio.setDocSpesaCod(rs.getString("doc_spesa_cod"));
		return dettaglio;

	}

}
