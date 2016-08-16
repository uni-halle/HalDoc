<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:date="http://exslt.org/dates-and-times" extension-element-prefixes="date"
	xmlns:svg="http://www.w3.org/2000/svg"
	xmlns:strClass="http://www.oracle.com/XSL/Transform/java/java.lang.String"
	xmlns:dyn="http://exslt.org/dynamic" version="1.0">

	<xsl:output method="xml" indent="yes" />
	<xsl:decimal-format name="FrenchDecimalFormat" decimal-separator="." grouping-separator="&#160;" />
	<xsl:decimal-format name="european" decimal-separator="." grouping-separator="," />
	<xsl:variable name="now" select="date:date-time()" />
	<xsl:variable name="today" select="date:date()" />

	<xsl:template match="publishDetail">

		<xsl:variable name="imagePath">file:[PUBROOT]img</xsl:variable>
		<!--xsl:variable name="imagePath">D:/Tomcats/loewenportal170/webapps/portal/pub/img</xsl:variable-->
		<xsl:variable name="viereckColor"><xsl:value-of select="Person/MyPromo/Einrichtung/farbe" /></xsl:variable>
		<xsl:variable name="font">ZapfHumanist601BT-Roman</xsl:variable>
		<xsl:variable name="fakId"><xsl:value-of select="Person/MyPromo/Einrichtung/eID" /></xsl:variable>
		<xsl:variable name="fakName">
			<xsl:choose>
				<xsl:when test="contains(Person/MyPromo/Einrichtung/ltxt,'Naturwissenschaftliche')">
					<xsl:call-template name="string-replace-all">
				    <xsl:with-param name="text" select="Person/MyPromo/Einrichtung/ltxt" />
				    <xsl:with-param name="replace">Naturwissenschaftliche</xsl:with-param>
				    <xsl:with-param name="by">Naturwissenschaftlichen</xsl:with-param>
				  </xsl:call-template>
				</xsl:when>
				<xsl:when test="contains(Person/MyPromo/Einrichtung/ltxt,'Theologische')">
					<xsl:call-template name="string-replace-all">
				    <xsl:with-param name="text" select="Person/MyPromo/Einrichtung/ltxt" />
				    <xsl:with-param name="replace">Theologische</xsl:with-param>
				    <xsl:with-param name="by">Theologischen</xsl:with-param>
				  </xsl:call-template>
				</xsl:when>
				<xsl:when test="contains(Person/MyPromo/Einrichtung/ltxt,'Wirtschaftswissenschaftliche')">
					<xsl:call-template name="string-replace-all">
				    <xsl:with-param name="text" select="Person/MyPromo/Einrichtung/ltxt" />
				    <xsl:with-param name="replace">Juristische und Wirtschaftswissenschaftliche</xsl:with-param>
				    <xsl:with-param name="by">Juristischen und Wirtschaftswissenschaftlichen</xsl:with-param>
				  </xsl:call-template>
				</xsl:when>
				<xsl:when test="contains(Person/MyPromo/Einrichtung/ltxt,'Medizinische')">
					<xsl:call-template name="string-replace-all">
				    <xsl:with-param name="text" select="Person/MyPromo/Einrichtung/ltxt" />
				    <xsl:with-param name="replace">Medizinische</xsl:with-param>
				    <xsl:with-param name="by">Medizinischen</xsl:with-param>
				  </xsl:call-template>
				</xsl:when>
				<xsl:when test="contains(Person/MyPromo/Einrichtung/ltxt,'Philosophische')">
					<xsl:call-template name="string-replace-all">
				    <xsl:with-param name="text" select="Person/MyPromo/Einrichtung/ltxt" />
				    <xsl:with-param name="replace">Philosophische</xsl:with-param>
				    <xsl:with-param name="by">Philosophischen</xsl:with-param>
				  </xsl:call-template>
				</xsl:when>
				<xsl:when test="contains(Person/MyPromo/Einrichtung/ltxt,'Zentrum')">
					<xsl:call-template name="string-replace-all">
				    <xsl:with-param name="text" select="Person/MyPromo/Einrichtung/ltxt" />
				    <xsl:with-param name="replace">Zentrum</xsl:with-param>
				    <xsl:with-param name="by">Zentrums</xsl:with-param>
				  </xsl:call-template>
				</xsl:when>
				<xsl:otherwise><xsl:value-of select="Person/MyPromo/Einrichtung/ltxt" /></xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="anrede">
			<xsl:choose>
				<xsl:when test="Person/Geschl='M'">Herr</xsl:when>
				<xsl:otherwise>Frau</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

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
					<fo:block-container absolute-position="auto" break-before="auto">
						<fo:block color="#000000" text-align="center" font-family="{$font}" font-size="14pt" padding-bottom="0.5em">
							Eröffnung eines Promotionsverfahren an der <xsl:value-of select="$fakName" /> (<xsl:value-of select="Person/MyPromo/Studiengang/ltxt" />)
						</fo:block>
						<fo:block color="#000000" text-align="center" font-family="{$font}" font-size="12pt" font-weight="bold" padding-bottom="1em">
							<xsl:value-of select="Person/MyPromo/Institut" />
						</fo:block>
						<fo:block border-bottom-width="1pt" border-bottom-style="solid" />
					</fo:block-container>
					<fo:block-container absolute-position="auto" break-before="auto">
						<fo:block color="#000000" text-align="center" font-family="{$font}" font-size="14pt" font-weight="bold" padding-top="1em" padding-bottom="1em">
							<xsl:value-of select="$anrede" /><xsl:text> </xsl:text><xsl:value-of select="Person/AkadGradVor" /><xsl:text> </xsl:text><xsl:value-of select="Person/PerVor" /><xsl:text> </xsl:text><xsl:value-of select="Person/PerNac" />
						</fo:block>
						<fo:block color="#000000" font-family="{$font}" font-size="12pt">
							<fo:table table-layout="fixed" width="16.5cm">
								<fo:table-column column-width="5cm"/>
								<fo:table-column column-width="11.5cm"/>
								<fo:table-body>
									<fo:table-row>
										<fo:table-cell><fo:block font-weight="bold" padding-bottom="0.5em">geboren am:</fo:block></fo:table-cell>
										<fo:table-cell><fo:block><xsl:value-of select="Person/GebDat" /> in <xsl:value-of select="Person/GebOrt" /></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell><fo:block font-weight="bold" padding-bottom="0.5em">erworbener Grad:</fo:block></fo:table-cell>
										<fo:table-cell><fo:block><xsl:value-of select="Person/AkadGradVor" /></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell><fo:block font-weight="bold" padding-bottom="0.5em">Status bei Antragstellung:</fo:block></fo:table-cell>
										<fo:table-cell><fo:block><xsl:value-of select="Person/PersonalStatusTxt" /></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell><fo:block font-weight="bold" padding-bottom="0.5em">Adresse:</fo:block></fo:table-cell>
										<fo:table-cell>
											<fo:block><xsl:value-of select="Person/Dienstadresse/Strasse" /></fo:block>
											<fo:block padding-bottom="0.5em"><xsl:value-of select="Person/Dienstadresse/PLZ" /><xsl:text> </xsl:text><xsl:value-of select="Person/Dienstadresse/Ort" /></fo:block>
										</fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell><fo:block font-weight="bold" padding-bottom="0.5em">Eröffnung durch die Fakultät am:</fo:block></fo:table-cell>
										<fo:table-cell><fo:block padding-bottom="0.5em"><xsl:value-of select="Person/MyPromo/Process/openingOfProcedure" /></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell><fo:block font-weight="bold" padding-bottom="0.5em">Thema der Dissertation:</fo:block></fo:table-cell>
										<fo:table-cell><fo:block padding-bottom="0.5em"><xsl:value-of select="Person/MyPromo/Titel" /></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell><fo:block font-weight="bold" padding-bottom="0.5em">wird durchgeführt im:</fo:block></fo:table-cell>
										<fo:table-cell><fo:block padding-bottom="0.5em"><xsl:value-of select="Person/MyPromo/Institut" /></fo:block></fo:table-cell>
									</fo:table-row>
									<fo:table-row>
										<fo:table-cell><fo:block font-weight="bold" padding-bottom="0.5em">Betreuer 1:</fo:block></fo:table-cell>
										<fo:table-cell><fo:block padding-bottom="0.5em"><xsl:value-of select="Person/MyPromo/Mentor1/Titel" /><xsl:text> </xsl:text><xsl:value-of select="Person/MyPromo/Mentor1/AkadGradVor" /><xsl:text> </xsl:text><xsl:value-of select="Person/MyPromo/Mentor1/nachname" /></fo:block></fo:table-cell>
									</fo:table-row>
									<xsl:if test="Person/MyPromo/Mentor2/nachname">
										<fo:table-row>
											<fo:table-cell><fo:block font-weight="bold" padding-bottom="0.5em">Betreuer 2:</fo:block></fo:table-cell>
											<fo:table-cell><fo:block padding-bottom="0.5em"><xsl:value-of select="Person/MyPromo/Mentor2/Titel" /><xsl:text> </xsl:text><xsl:value-of select="Person/MyPromo/Mentor2/AkadGradVor" /><xsl:text> </xsl:text><xsl:value-of select="Person/MyPromo/Mentor2/nachname" /></fo:block></fo:table-cell>
										</fo:table-row>
									</xsl:if>
									<xsl:if test="Person/MyPromo/Mentor3/nachname">
										<fo:table-row>
											<fo:table-cell><fo:block font-weight="bold" padding-bottom="0.5em">Betreuer 3:</fo:block></fo:table-cell>
											<fo:table-cell><fo:block padding-bottom="0.5em"><xsl:value-of select="Person/MyPromo/Mentor3/Titel" /><xsl:text> </xsl:text><xsl:value-of select="Person/MyPromo/Mentor3/AkadGradVor" /><xsl:text> </xsl:text><xsl:value-of select="Person/MyPromo/Mentor3/nachname" /></fo:block></fo:table-cell>
										</fo:table-row>
									</xsl:if>
									<xsl:for-each select="Person/MyPromo/Evaluator">
										<fo:table-row>
											<fo:table-cell><fo:block font-weight="bold" padding-bottom="0.5em">Gutachter <xsl:value-of select="Nr" />:</fo:block></fo:table-cell>
											<fo:table-cell>
												<fo:block><xsl:value-of select="Titel" /><xsl:text> </xsl:text><xsl:value-of select="AkadGradVor" /><xsl:text> </xsl:text><xsl:value-of select="nachname" /></fo:block>
												<xsl:if test="Address/einrichtung != ''"><fo:block><xsl:value-of select="Address/einrichtung" /></fo:block></xsl:if>
												<xsl:if test="Address/Institut != ''"><fo:block><xsl:value-of select="Address/Institut" /></fo:block></xsl:if>
												<xsl:if test="Address/adrzusatz != ''"><fo:block><xsl:value-of select="Address/adrzusatz" /></fo:block></xsl:if>
												<fo:block><xsl:value-of select="Address/strasse" /></fo:block>
												<fo:block padding-bottom="0.5em"><xsl:value-of select="Address/plz" /><xsl:text> </xsl:text><xsl:value-of select="Address/ort" /></fo:block>
											</fo:table-cell>
										</fo:table-row>
									</xsl:for-each>
									<xsl:if test="Person/MyPromo/Vorsitz/nachname">
										<fo:table-row>
											<fo:table-cell><fo:block font-weight="bold" padding-bottom="0.5em">Vorsitzender PK:</fo:block></fo:table-cell>
											<fo:table-cell><fo:block padding-bottom="0.5em"><xsl:value-of select="Person/MyPromo/Vorsitz/Titel" /><xsl:text> </xsl:text><xsl:value-of select="Person/MyPromo/Vorsitz/AkadGradVor" /><xsl:text> </xsl:text><xsl:value-of select="Person/MyPromo/Vorsitz/nachname" /></fo:block></fo:table-cell>
										</fo:table-row>
									</xsl:if>
									<fo:table-row>
										<fo:table-cell><fo:block font-weight="bold" padding-bottom="0.5em">Promotionskommission:</fo:block></fo:table-cell>
										<fo:table-cell>
											<fo:block border-bottom-width="1pt" border-bottom-style="solid" padding-bottom="1.5em"><xsl:text> </xsl:text></fo:block>
											<fo:block border-bottom-width="1pt" border-bottom-style="solid" padding-bottom="1.5em"><xsl:text> </xsl:text></fo:block>
											<fo:block border-bottom-width="1pt" border-bottom-style="solid" padding-bottom="1.5em"><xsl:text> </xsl:text></fo:block>
										</fo:table-cell>
									</fo:table-row>
								</fo:table-body>
							</fo:table>
						</fo:block>
					</fo:block-container>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>

	<xsl:template name="string-replace-all">
	  <xsl:param name="text" />
	  <xsl:param name="replace" />
	  <xsl:param name="by" />
	  <xsl:choose>
	    <xsl:when test="contains($text, $replace)">
	      <xsl:value-of select="substring-before($text,$replace)" />
	      <xsl:value-of select="$by" />
	      <xsl:call-template name="string-replace-all">
	        <xsl:with-param name="text" select="substring-after($text,$replace)" />
	        <xsl:with-param name="replace" select="$replace" />
	        <xsl:with-param name="by" select="$by" />
	      </xsl:call-template>
	    </xsl:when>
	    <xsl:otherwise>
	      <xsl:value-of select="$text" />
	    </xsl:otherwise>
	  </xsl:choose>
	</xsl:template>
</xsl:stylesheet>
