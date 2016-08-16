<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:date="http://exslt.org/dates-and-times" extension-element-prefixes="date"
	xmlns:svg="http://www.w3.org/2000/svg"
	xmlns:dyn="http://exslt.org/dynamic" version="1.0">

	<xsl:output method="xml" indent="yes" />
	<xsl:decimal-format name="FrenchDecimalFormat" decimal-separator="." grouping-separator="&#160;" />
	<xsl:decimal-format name="european" decimal-separator="." grouping-separator="," />
	<xsl:variable name="now" select="date:date-time()" />

	<xsl:template match="publishDetail">
		<xsl:variable name="font">ZapfHumanist601BT-Roman</xsl:variable>

		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master master-name="ersteSeite" page-height="297mm" page-width="210mm">
					<fo:region-body region-name="Body" margin-top="15mm" margin-left="30mm" margin-right="15mm" margin-bottom="15mm" background-color="white" />
					<fo:region-before region-name="KopfSeite1" extent="14mm" precedence="true" background-color="white" />
					<fo:region-after region-name="Fuss" extent="15mm" background-color="white" />
					<fo:region-start region-name="LRand" extent="50mm" background-color="white" />
					<fo:region-end region-name="RRand" extent="15mm" background-color="white" />
				</fo:simple-page-master>
				<fo:simple-page-master master-name="weitereSeiten" page-height="297mm" page-width="210mm">
					<fo:region-body region-name="Body" margin-top="15mm" margin-left="30mm" margin-right="15mm" margin-bottom="15mm" background-color="white" />
					<fo:region-before region-name="Kopf" extent="14mm" precedence="true" background-color="white" />
					<fo:region-after region-name="Fuss" extent="15mm" background-color="white" />
					<fo:region-start region-name="LRand" extent="50mm" background-color="white" />
					<fo:region-end region-name="RRand" extent="15mm" background-color="white" />
				</fo:simple-page-master>
				<fo:page-sequence-master master-name="global">
					<fo:single-page-master-reference master-reference="ersteSeite" />
					<fo:repeatable-page-master-reference master-reference="weitereSeiten" />
				</fo:page-sequence-master>
			</fo:layout-master-set>

			<fo:page-sequence master-reference="global">
				<fo:static-content flow-name="KopfSeite1">
					<fo:block-container position="absolute" left="15mm" top="4mm">
						<fo:block />
					</fo:block-container>
				</fo:static-content>

				<fo:static-content flow-name="Kopf">
					<fo:block-container position="absolute" left="15mm" top="4mm">
						<fo:block />
					</fo:block-container>
				</fo:static-content>

				<!-- Falzmarken immer am linken Rand -->
				<fo:static-content flow-name="LRand">
					<fo:block-container absolute-position="fixed" top="101.5mm" left="3.5mm">
						<fo:block>
							<fo:leader leader-pattern="rule" rule-thickness="0.5pt" leader-length="3mm" />
						</fo:block>
						<fo:block padding-top="38.5mm">
							<fo:leader leader-pattern="rule" rule-thickness="0.5pt" leader-length="6mm" />
						</fo:block>
						<fo:block padding-top="56.5mm">
							<fo:leader leader-pattern="rule" rule-thickness="0.5pt" leader-length="3mm" />
						</fo:block>
					</fo:block-container>
				</fo:static-content>

				<fo:static-content flow-name="Fuss">
					<fo:block-container position="absolute" left="15mm" top="4mm">
						<fo:block />
					</fo:block-container>
				</fo:static-content>

				<fo:flow flow-name="Body" font-family="{$font}" font-size="12pt">
					<fo:block-container absolute-position="auto" break-before="auto">
						<fo:block color="#000000" text-align="center" font-family="{$font}" font-size="14pt" font-weight="bold" padding-top="5em" padding-bottom="5em">
							Bescheinigung zur Abgabe der Pflichtexemplare in der Universitäts- und Landesbibliothek
						</fo:block>
						<fo:block color="#000000" font-family="{$font}" text-align="justify" hyphenate="true" language="de" padding-bottom="2em">
							Hiermit wird bescheinigt, dass
						</fo:block>
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
							<fo:block>
								<xsl:variable name="anrede">
									<xsl:choose>
										<xsl:when test="promotion/Geschl='M'">Herr</xsl:when>
										<xsl:otherwise>Frau</xsl:otherwise>
									</xsl:choose>
								</xsl:variable>
								<xsl:value-of select="$anrede" /><xsl:text> </xsl:text><xsl:value-of select="promotion/PerVor" /><xsl:text> </xsl:text><xsl:value-of select="promotion/PerNac" />
							</fo:block>
						</fo:block>
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
							<fo:block>
								<xsl:variable name="pronomen">
									<xsl:choose>
										<xsl:when test="promotion/Geschl='M'">seiner</xsl:when>
										<xsl:otherwise>ihrer</xsl:otherwise>
									</xsl:choose>
								</xsl:variable>
								<xsl:text>von </xsl:text><xsl:value-of select="$pronomen" /><xsl:text> Dissertation/Promotion</xsl:text>
							</fo:block>
						</fo:block>
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
							<fo:block>Titel: <xsl:value-of select="promotion/MyPromo/Titel" /></fo:block>
						</fo:block>
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
							<xsl:choose>
								<xsl:when test="promotion/MyPromo/Process/handoverType = '1'">
									<fo:block><xsl:value-of select="promotion/MyPromo/Process/handoverCount" /> Pflichtexemplare laut Promotionsordnung</fo:block>
								</xsl:when>
								<xsl:when test="promotion/MyPromo/Process/handoverType = '2'">
									<fo:block><xsl:value-of select="promotion/MyPromo/Process/handoverCount" /> Verlagsausgaben laut Promotionsordnung</fo:block>
								</xsl:when>
								<xsl:otherwise>
									<xsl:variable name="elekCount">
										<xsl:choose>
											<xsl:when test="normalize-space(promotion/MyPromo/Process/handoverCount) != ''"><xsl:value-of select="promotion/MyPromo/Process/handoverCount" /></xsl:when>
											<xsl:otherwise>1</xsl:otherwise>
										</xsl:choose>
									</xsl:variable>
									<fo:block><xsl:value-of select="$elekCount" /> elektronische Version und <xsl:value-of select="$elekCount" /> Archivexemplar entsprechend der Regelungen der ULB</fo:block>
								</xsl:otherwise>
							</xsl:choose>
						</fo:block>
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="5em">
							<fo:block>in der Universitäts- und Landesbibliothek Sachsen-Anhalt abgegeben hat.</fo:block>
						</fo:block>
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="8em">
							<fo:block>Universitäts- und Landesbibliothek Sachsen-Anhalt</fo:block>
							<fo:block>Hochschulschriftenstelle</fo:block>
						</fo:block>
						<fo:block color="#000000" text-align="start" font-family="{$font}" hyphenate="true" language="de" padding-bottom="2em">
							<fo:block>Halle, den <xsl:value-of select="date:format-date($now,'dd.MM.yyyy')" /></fo:block>
						</fo:block>
					</fo:block-container>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>
