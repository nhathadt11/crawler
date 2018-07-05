<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <xsl:output omit-xml-declaration="yes" indent="yes" />

  <xsl:template match="//div[@class='group-detail']">
    <movie>
      <title>
        <xsl:value-of select="normalize-space(//h1[@class='title-film-detail-1']/text())"/>
      </title>

      <year>
        <xsl:value-of select="substring(//ul[@class='infomation-film']/li[2]/span/text(), 7)"/>
      </year>

      <genre>
        <xsl:call-template name="genre">
          <xsl:with-param name="genres" select="//ul[@class='infomation-film']/li[7]"/>
        </xsl:call-template>
      </genre>

      <country>
        <xsl:value-of select="//ul[@class='infomation-film']/li[8]/a"/>
      </country>

      <xsl:call-template name="duration" />

      <director>
        <xsl:value-of select="//ul[@class='infomation-film']/li[5]/span"/>
      </director>

      <xsl:call-template name="rating"/>

      <plot>
        <xsl:value-of select="//p[@class='content-film']"/>
      </plot>

      <stars>
        <xsl:call-template name="collect-stars">
          <xsl:with-param name="stars" select="//ul[@class='infomation-film']/li[6]"/>
        </xsl:call-template>
      </stars>

      <image>
        <xsl:value-of select="//img[@itemprop='image']/@src" />
      </image>
    </movie>
  </xsl:template>

  <xsl:template name="genre">
    <xsl:param name="genres"/>
    <xsl:for-each select="$genres/a/text()">
      <xsl:value-of select="concat(self::text(), ', ')"/>
    </xsl:for-each>
  </xsl:template>

  <xsl:template name="duration">
    <xsl:variable name="durationWithUnit" select="//ul[@class='infomation-film']/li[3]/span/text()"/>
    <xsl:choose>
      <xsl:when test="not(contains($durationWithUnit, 'N/A'))">
        <duration>
          <xsl:value-of select="substring-before($durationWithUnit, ' ')"/>
        </duration>
      </xsl:when>
      <xsl:otherwise>
        <duration xsi:nil="true" />
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="rating">
    <xsl:variable name="rating" select="substring-after(//div[@class='imdb'], ' ')"/>
    <xsl:choose>
      <xsl:when test="not(contains($rating, 'N/A'))">
        <rating>
          <xsl:value-of select="$rating"/>
        </rating>
      </xsl:when>
      <xsl:otherwise>
        <rating xsi:nil="true" />
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="collect-stars">
    <xsl:param name="stars" />
    <xsl:for-each select="$stars/a/text()">
      <xsl:value-of select="concat(self::text(), ', ')"/>
    </xsl:for-each>
  </xsl:template>
</xsl:stylesheet>