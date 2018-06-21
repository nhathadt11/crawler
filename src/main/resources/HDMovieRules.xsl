<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output omit-xml-declaration="yes" indent="yes" />

  <xsl:template match="//div[@class='block-movie']">
    <movie>
      <title>
        <xsl:value-of select="//ul[@class='filminfo-fields']//li[2]//strong"/>
      </title>

      <year>
        <xsl:value-of select="//ul[@class='filminfo-fields']//li[4]/a"/>
      </year>

      <genre>
        <xsl:value-of select="//ul[@class='filminfo-fields']//li[5]/strong"/>
      </genre>

      <country>
        <xsl:value-of select="//ul[@class='filminfo-fields']//li[6]//a"/>
      </country>

      <duration>
        <xsl:apply-templates select="//ul[@class='filminfo-fields']//li[7]"/>
      </duration>

      <director>
        <xsl:value-of select="//ul[@class='filminfo-fields']//li[8]//a"/>
      </director>

      <rating>
        <xsl:apply-templates select="//ul[@class='filminfo-fields']//li[9]"/>
      </rating>

      <plot>
        <xsl:value-of select="//div[@itemprop='description']"/>
      </plot>

      <stars>
        <xsl:call-template name="collect-stars">
          <xsl:with-param name="stars" select="//div[@class='group-filminfo']/div" />
        </xsl:call-template>
      </stars>
    </movie>
  </xsl:template>

  <xsl:template match="//ul[@class='filminfo-fields']//li[7]">
    <xsl:variable name="durationWithUnit" select="substring-after(text(), ': ')"/>
    <xsl:value-of select="substring-before($durationWithUnit, ' ')"/>
  </xsl:template>

  <xsl:template match="//ul[@class='filminfo-fields']//li[9]">
    <xsl:value-of select="normalize-space(.)"/>
  </xsl:template>

  <xsl:template name="collect-stars">
    <xsl:param name="stars" />
    <xsl:for-each select="$stars//li//span/a/text()">
      <xsl:value-of select="concat(self::text(), ', ')"/>
    </xsl:for-each>
  </xsl:template>
</xsl:stylesheet>