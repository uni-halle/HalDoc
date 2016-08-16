<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:date="http://exslt.org/dates-and-times" extension-element-prefixes="date"
	xmlns:svg="http://www.w3.org/2000/svg"
	xmlns:strClass="http://www.oracle.com/XSL/Transform/java/java.lang.String"
	xmlns:dyn="http://exslt.org/dynamic" version="1.0">

	<xsl:output method="xml" indent="yes" />
	<xsl:decimal-format name="FrenchDecimalFormat" decimal-separator="." grouping-separator="&#160;" />
	<xsl:decimal-format name="european" decimal-separator="." grouping-separator="," />
	<xsl:variable name="now" select="date:date-time()" />

	<xsl:template match="publishDetail">

		<xsl:variable name="imagePath">file:[PUBROOT]img</xsl:variable>
		<!--xsl:variable name="imagePath">D:/Tomcats/tomcat-6.0.39-14.0/webapps/qisserver/pub/img</xsl:variable-->
		<xsl:variable name="viereckColor"><xsl:value-of select="Person/MyInfos/Einrichtung/farbe" /></xsl:variable>
		<xsl:variable name="font">ZapfHumanist601BT-Roman</xsl:variable>
		<xsl:variable name="printLang"><xsl:value-of select="Person/Form/Language" /></xsl:variable>

		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master master-name="ersteSeite" page-height="297mm" page-width="210mm">
					<fo:region-body region-name="Body" margin-top="11mm" margin-left="27.5mm" margin-right="7.5mm" margin-bottom="53mm" background-color="white" /> <!--background-image="{$imagePath}/wasserzeichen_siegel_Halle.gif"
						background-repeat="no-repeat" /-->
					<fo:region-before region-name="Kopf" extent="11mm" precedence="true" background-color="white" />
					<fo:region-after region-name="Fuss" extent="53mm" background-color="white" />
					<fo:region-start region-name="LRand" extent="13mm" background-color="white" />
					<fo:region-end region-name="RRand" extent="7.5mm" background-color="white" />
				</fo:simple-page-master>
				<fo:page-sequence-master master-name="global">
					<fo:single-page-master-reference master-reference="ersteSeite" />
					<!--fo:repeatable-page-master-reference master-reference="ersteSeite" /-->
				</fo:page-sequence-master>
			</fo:layout-master-set>

			<fo:page-sequence master-reference="global">
				<fo:static-content flow-name="Kopf">
					<fo:block-container position="absolute" left="0mm" top="0mm" height="11mm" width="13mm" background-color="#{$viereckColor}" border-bottom-width="1pt" border-bottom-style="solid" border-bottom-color="#{$viereckColor}">
						<fo:block />
					</fo:block-container>
				</fo:static-content>

				<!-- Falzmarken immer am linken Rand -->
				<fo:static-content flow-name="LRand">
					<fo:block-container position="absolute" left="0mm" top="0mm" height="233mm" width="13mm" background-color="#{$viereckColor}" border-top-width="1pt" border-top-style="solid" border-top-color="#{$viereckColor}">
						<fo:block />
					</fo:block-container>
				</fo:static-content>

				<fo:static-content flow-name="Fuss">
					<fo:block-container position="absolute" left="0mm" top="13mm" height="21mm" width="50mm" background-color="white">
						<fo:block>
							<fo:external-graphic src="{$imagePath}/image_siegel_Halle.gif" content-height="21mm" />
						</fo:block>
					</fo:block-container>
					<fo:block-container position="absolute" left="50mm" top="21mm" height="13mm" width="80mm" background-color="white">
						<fo:block font-family="{$font}" font-size="14pt">
							MARTIN-LUTHER-UNIVERSITÄT
						</fo:block>
						<fo:block font-family="{$font}" font-size="14pt">
							HALLE-WITTENBERG
						</fo:block>
					</fo:block-container>
				</fo:static-content>

				<fo:flow flow-name="Body" font-family="{$font}" font-size="10pt">
					<fo:block-container absolute-position="absolute" top="5mm" left="5mm" width="165mm">
						<fo:block color="#000000" text-align="center" font-family="{$font}" font-size="14pt" font-weight="bold">
							<xsl:variable name="doktorand">
								<xsl:if test="Person/Geschl='M'">
									<xsl:text>Herrn </xsl:text>
								</xsl:if>
								<xsl:if test="Person/Geschl='W'">
									<xsl:text>Frau </xsl:text>
								</xsl:if>
								<xsl:value-of select="Person/PerNac" />
							</xsl:variable>
							Dissertation von <xsl:value-of select="$doktorand" /> und Gutachten
							<fo:block />
							zur Einsichtnahme
						</fo:block>
						<fo:block color="#000000" padding-top="1em" text-align="center" font-family="{$font}" font-size="12pt" font-weight="bold">
							durch die Hochschullehrer/innen und habilitierten Mitarbeiter/innen
						</fo:block>
						<fo:block color="#000000" padding-top="2em" text-align="start" font-family="{$font}" font-size="12pt" hyphenate="true" language="de">
							Auslage vom <xsl:value-of select="Person/Form/AuslageVon" /> bis <xsl:value-of select="Person/Form/AuslageBis" /> im Dekanat der <xsl:value-of select="Person/MyInfos/Einrichtung/ltxt" />
						</fo:block>
						<fo:block color="#000000" padding-top="2em" text-align="center" font-family="{$font}">
							<fo:table table-layout="fixed">
								<fo:table-column column-width="15mm" />
								<fo:table-column column-width="75mm" />
								<fo:table-column column-width="75mm" />
								<fo:table-body>
									<fo:table-row>
										<fo:table-cell border-bottom-width="0.5pt" border-bottom-style="solid">
											<fo:block>
												Lfd. Nr.
											</fo:block>
										</fo:table-cell>
										<fo:table-cell border-bottom-width="0.5pt" border-bottom-style="solid">
											<fo:block>
												Name
											</fo:block>
										</fo:table-cell>
										<fo:table-cell border-bottom-width="0.5pt" border-bottom-style="solid">
											<fo:block>
												Unterschrift
											</fo:block>
										</fo:table-cell>
									</fo:table-row>
									<xsl:call-template name="getRow">
										<xsl:with-param name="num">1</xsl:with-param>
										<xsl:with-param name="max">25</xsl:with-param>
									</xsl:call-template>
								</fo:table-body>
							</fo:table>
						</fo:block>
					</fo:block-container>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>

	<xsl:template name="getRow">
		<xsl:param name="num">1</xsl:param> <!-- param has initial value of	1 -->
		<xsl:param name="max">10</xsl:param> <!-- param has initial value of	10 -->
		<xsl:if test="not($num = $max)">
			<fo:table-row>
				<fo:table-cell border-bottom-width="0.5pt" border-bottom-style="solid" padding-right="9mm" text-align="end" height="2em">
					<fo:block><xsl:value-of select="$num" /></fo:block>
				</fo:table-cell>
				<fo:table-cell border-bottom-width="0.5pt" border-bottom-style="solid" height="2em" number-columns-spanned="2">
					<fo:block />
				</fo:table-cell>
			</fo:table-row>
			<xsl:call-template name="getRow">
				<xsl:with-param name="num">
					<xsl:value-of select="$num + 1" />
				</xsl:with-param>
				<xsl:with-param name="max">
					<xsl:value-of select="$max" />
				</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
<!-- AKTUELL:{$font} -->
<!-- vim: set encoding=utf-8 syntax=on tabstop=2 expandtab filetype=html background= : -->
