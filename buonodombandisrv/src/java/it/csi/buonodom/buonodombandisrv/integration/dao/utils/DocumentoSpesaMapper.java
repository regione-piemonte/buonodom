/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombandisrv.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import it.csi.buonodom.buonodombandisrv.dto.ModelDocumentoSpesa;
import it.csi.buonodom.buonodombandisrv.util.Converter;
import it.csi.buonodom.buonodombandisrv.util.Util;

public class DocumentoSpesaMapper implements RowMapper<ModelDocumentoSpesa> {

	@Override
	public ModelDocumentoSpesa mapRow(ResultSet rs, int rowNum) throws SQLException {

		ModelDocumentoSpesa spesa = new ModelDocumentoSpesa();
		spesa.setId(rs.getInt("doc_spesa_id"));
		spesa.setIdFornitore(rs.getInt("fornitore_id"));
		spesa.setNote(rs.getString("doc_spesa_stato_note"));
		spesa.setNumero(rs.getString("doc_numero"));
		spesa.setStato(rs.getString("doc_spesa_stato_cod"));
		spesa.setTipologia(rs.getString("doc_spesa_tipo_cod"));
		spesa.setDicSpesaCod(rs.getString("dic_spesa_cod"));
		spesa.setFornitoreCf(rs.getString("fornitore_cf"));
		List<String> mesi = new ArrayList<String>();
		HashSet<String> hset = new HashSet<String>();
		Date periodoinizio = Converter.getDataWithoutTime(rs.getString("doc_spesa_periodo_inizio") + "-01");
		Date periodofine = Converter.getDataWithoutTime(rs.getString("doc_spesa_periodo_fine") + "-01");
		int numeromesi = Util.getNumeroMesiDifferenzaDate(periodoinizio, periodofine);
		hset.add(rs.getString("doc_spesa_periodo_inizio"));
		for (int i = 1; i < numeromesi; i++) {
			hset.add(Converter.getDataNoDay(Converter.aggiungiMesiAData(periodoinizio, i)));
		}
		hset.add(rs.getString("doc_spesa_periodo_fine"));
		mesi.addAll(hset);
		spesa.setMesi(mesi);
		return spesa;
	}

}
