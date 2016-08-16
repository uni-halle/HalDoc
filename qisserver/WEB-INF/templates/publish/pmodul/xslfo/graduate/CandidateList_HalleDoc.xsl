<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:date="http://exslt.org/dates-and-times" xmlns="urn:schemas-microsoft-com:office:spreadsheet" xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet" xmlns:html="http://www.w3.org/TR/REC-html40" version="1.0">
	<xsl:output method="xml" indent="yes" encoding="UTF-8" />
	<xsl:preserve-space elements="*" />
	<xsl:variable name="now" select="date:date-time()" />

	<xsl:template match="Promotion">
		<Row page-break-after="always">
			<Cell ss:StyleID="s12" page-break-after="always">
				<Data ss:Type="String" page-break-after="always">
					<xsl:value-of select="pID" />
				</Data>
			</Cell>
			<Cell ss:StyleID="s12">
				<Data ss:Type="String">
					<xsl:value-of select="vorname" />
				</Data>
			</Cell>
			<Cell ss:StyleID="s12">
				<Data ss:Type="String">
					<xsl:value-of select="nachname" />
				</Data>
			</Cell>
			<Cell ss:StyleID="s12">
				<Data ss:Type="String">
					<xsl:value-of select="Strasse" />
				</Data>
			</Cell>
			<Cell ss:StyleID="s12">
				<Data ss:Type="String">
					<xsl:value-of select="PLZ" />
				</Data>
			</Cell>
			<Cell ss:StyleID="s12">
				<Data ss:Type="String">
					<xsl:value-of select="Ort" />
				</Data>
			</Cell>
			<Cell ss:StyleID="s12">
				<Data ss:Type="String">
					<xsl:value-of select="Zusatz" />
				</Data>
			</Cell>
			<Cell ss:StyleID="s12">
				<Data ss:Type="String">
					<xsl:value-of select="Email" />
				</Data>
			</Cell>
			<Cell ss:StyleID="s12">
				<Data ss:Type="String">
					<xsl:value-of select="Telefon" />
				</Data>
			</Cell>
			<Cell ss:StyleID="s12">
				<Data ss:Type="String">
					<xsl:value-of select="GebDat" />
				</Data>
			</Cell>
			<Cell ss:StyleID="s12">
				<Data ss:Type="String">
					<xsl:value-of select="Antrag" />
				</Data>
			</Cell>
			<Cell ss:StyleID="s12">
				<Data ss:Type="String">
					<xsl:value-of select="Annahme" />
				</Data>
			</Cell>
			<Cell ss:StyleID="s12">
				<Data ss:Type="String">
					<xsl:value-of select="colloquium" />
				</Data>
			</Cell>
			<Cell ss:StyleID="s12">
				<Data ss:Type="String">
					<xsl:value-of select="GesNote" />
				</Data>
			</Cell>
			<Cell ss:StyleID="s12">
				<Data ss:Type="String">
					<xsl:value-of select="GesPr" />
				</Data>
			</Cell>
			<xsl:if test="Mentor1Info">
				<Cell ss:StyleID="s12">
					<Data ss:Type="String">
						<xsl:choose>
							<xsl:when test="Mentor1Info/geschl='M'"><xsl:text>Herr </xsl:text></xsl:when>
							<xsl:otherwise><xsl:text>Frau </xsl:text></xsl:otherwise>
						</xsl:choose>
						<xsl:if test="Mentor1Info/Titel"><xsl:value-of select="Mentor1Info/Titel" /><xsl:text> </xsl:text></xsl:if>
						<xsl:if test="Mentor1Info/AkadGrad"><xsl:value-of select="Mentor1Info/AkadGrad" /><xsl:text> </xsl:text></xsl:if>
						<xsl:value-of select="Mentor1Info/vorname" /><xsl:text> </xsl:text><xsl:value-of select="Mentor1Info/nachname" />
					</Data>
				</Cell>
			</xsl:if>
			<xsl:if test="Mentor2Info">
				<Cell ss:StyleID="s12">
					<Data ss:Type="String">
						<xsl:choose>
							<xsl:when test="Mentor2Info/geschl='M'"><xsl:text>Herr </xsl:text></xsl:when>
							<xsl:otherwise><xsl:text>Frau </xsl:text></xsl:otherwise>
						</xsl:choose>
						<xsl:if test="Mentor2Info/Titel"><xsl:value-of select="Mentor2Info/Titel" /><xsl:text> </xsl:text></xsl:if>
						<xsl:if test="Mentor2Info/AkadGrad"><xsl:value-of select="Mentor2Info/AkadGrad" /><xsl:text> </xsl:text></xsl:if>
						<xsl:value-of select="Mentor2Info/vorname" /><xsl:text> </xsl:text><xsl:value-of select="Mentor2Info/nachname" />
					</Data>
				</Cell>
			</xsl:if>
			<xsl:if test="Mentor3Info">
				<Cell ss:StyleID="s12">
					<Data ss:Type="String">
						<xsl:choose>
							<xsl:when test="Mentor3Info/geschl='M'"><xsl:text>Herr </xsl:text></xsl:when>
							<xsl:otherwise><xsl:text>Frau </xsl:text></xsl:otherwise>
						</xsl:choose>
						<xsl:if test="Mentor3Info/Titel"><xsl:value-of select="Mentor3Info/Titel" /><xsl:text> </xsl:text></xsl:if>
						<xsl:if test="Mentor3Info/AkadGrad"><xsl:value-of select="Mentor3Info/AkadGrad" /><xsl:text> </xsl:text></xsl:if>
						<xsl:value-of select="Mentor3Info/vorname" /><xsl:text> </xsl:text><xsl:value-of select="Mentor3Info/nachname" />
					</Data>
				</Cell>
			</xsl:if>
		</Row>
	</xsl:template>

	<xsl:template match="/">
		<xsl:processing-instruction name='mso-application'>
			progid="Excel.Sheet"
		</xsl:processing-instruction>
		<Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet" xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet" xmlns:html="http://www.w3.org/TR/REC-html40">
			<ExcelWorkbook xmlns="urn:schemas-microsoft-com:office:excel">
				<ProtectStructure>False</ProtectStructure>
				<ProtectWindows>False</ProtectWindows>
			</ExcelWorkbook>
			<xsl:variable name="now" select="date:date-time()" />

			<Styles>
				<Style ss:ID="Default" ss:Name="Normal">
					<Alignment ss:Vertical="Bottom" />
					<Borders />
					<Font />
					<Interior />
					<NumberFormat />
					<Protection />
				</Style>

				<Style ss:ID="s1">
					<Font ss:FontName="Arial" x:Family="Swiss" ss:Color="#000000" ss:Size="10" ss:Bold="1" />
					<Alignment ss:Horizontal="Center" ss:Vertical="Center" />
				</Style>
				<Style ss:ID="s12">
					<Font ss:FontName="Arial" x:Family="Swiss" ss:Color="#000000" ss:Size="10" />
				</Style>
			</Styles>


			<Worksheet ss:Name="Promovierende">
				<Table>
					<Column ss:AutoFitWidth="0" ss:Width="53" />
					<Column ss:AutoFitWidth="0" ss:Width="80" />
					<Column ss:AutoFitWidth="0" ss:Width="80" />
					<Column ss:AutoFitWidth="0" ss:Width="80" />
					<Column ss:AutoFitWidth="0" ss:Width="42" />
					<Column ss:AutoFitWidth="0" ss:Width="80" />
					<Column ss:AutoFitWidth="0" ss:Width="80" />
					<Column ss:AutoFitWidth="0" ss:Width="80" />
					<Column ss:AutoFitWidth="0" ss:Width="80" />
					<Column ss:AutoFitWidth="0" ss:Width="80" />
					<Column ss:AutoFitWidth="1" ss:Width="80" />
					<Column ss:AutoFitWidth="1" ss:Width="100" />
					<Column ss:AutoFitWidth="1" ss:Width="80" />
					<Column ss:AutoFitWidth="1" ss:Width="80" />
					<Column ss:AutoFitWidth="1" ss:Width="80" />
					<Column ss:AutoFitWidth="1" ss:Width="194" />
					<Column ss:AutoFitWidth="1" ss:Width="194" />
					<Column ss:AutoFitWidth="1" ss:Width="80" />
					<Row>
						<Cell ss:StyleID="s1">
							<Data ss:Type="String">Mtknr</Data>
						</Cell>
						<Cell ss:StyleID="s1">
							<Data ss:Type="String">Vorname</Data>
						</Cell>
						<Cell ss:StyleID="s1">
							<Data ss:Type="String">Nachname</Data>
						</Cell>
						<Cell ss:StyleID="s1">
							<Data ss:Type="String">Straße</Data>
						</Cell>
						<Cell ss:StyleID="s1">
							<Data ss:Type="String">PLZ</Data>
						</Cell>
						<Cell ss:StyleID="s1">
							<Data ss:Type="String">Ort</Data>
						</Cell>
						<Cell ss:StyleID="s1">
							<Data ss:Type="String">Zusatz</Data>
						</Cell>
						<Cell ss:StyleID="s1">
							<Data ss:Type="String">Email</Data>
						</Cell>
						<Cell ss:StyleID="s1">
							<Data ss:Type="String">Telefon</Data>
						</Cell>
						<Cell ss:StyleID="s1">
							<Data ss:Type="String">Geburtsdatum</Data>
						</Cell>
						<Cell ss:StyleID="s1">
							<Data ss:Type="String">Antrag gestellt</Data>
						</Cell>
						<Cell ss:StyleID="s1">
							<Data ss:Type="String">Antrag angenommen</Data>
						</Cell>
						<Cell ss:StyleID="s1">
							<Data ss:Type="String">Verteidigung</Data>
						</Cell>
						<Cell ss:StyleID="s1">
							<Data ss:Type="String">Gesamtnote</Data>
						</Cell>
						<Cell ss:StyleID="s1">
							<Data ss:Type="String">Prädikat</Data>
						</Cell>
						<Cell ss:StyleID="s1">
							<Data ss:Type="String">Betreuer 1</Data>
						</Cell>
						<Cell ss:StyleID="s1">
							<Data ss:Type="String">Betreuer 2</Data>
						</Cell>
						<Cell ss:StyleID="s1">
							<Data ss:Type="String">Betreuer 3</Data>
						</Cell>
					</Row>
					<xsl:apply-templates />
				</Table>

				<WorksheetOptions xmlns="urn:schemas-microsoft-com:office:excel">
					<PageSetup>
						<Header x:Margin="0.3" x:Data="&amp;Z&amp;&quot;Arial,Fett&quot;&amp;14Promovierendenliste aus HalDoc" />
						<Footer x:Margin="0.3" x:Data="&amp;LUni Halle&amp;D" />
						<Layout x:Orientation="Landscape" />
						<PageMargins x:Bottom="0.6" x:Left="0.7" x:Right="0.7" x:Top="0.6" />
					</PageSetup>

					<Print>
						<ValidPrinterInfo />
						<PaperSizeIndex>9</PaperSizeIndex>
						<HorizontalResolution>600</HorizontalResolution>
						<VerticalResolution>600</VerticalResolution>
					</Print>
					<Selected />
					<LeftColumnVisible>0</LeftColumnVisible>
					<ProtectObjects>False</ProtectObjects>
					<ProtectScenarios>False</ProtectScenarios>
				</WorksheetOptions>
			</Worksheet>





		</Workbook>
	</xsl:template>
</xsl:stylesheet>

