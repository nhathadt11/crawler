<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <xsl:output omit-xml-declaration="yes" indent="yes" />

  <xsl:template match="//div[@class='block-movie']">
    <movie>
      <title>
        <xsl:call-template name="title">
          <xsl:with-param name="title" select="//ul[@class='filminfo-fields']/li[contains(text(), 'Tên Phim')]"/>
        </xsl:call-template>
      </title>

      <year>
        <xsl:call-template name="year">
          <xsl:with-param name="year" select="//ul[@class='filminfo-fields']/li[contains(text(), 'Năm sản xuất')]"/>
        </xsl:call-template>
      </year>

      <genre>
        <xsl:call-template name="genre">
          <xsl:with-param name="genre" select="//ul[@class='filminfo-fields']/li[contains(text(), 'Thể loại')]"/>
        </xsl:call-template>
      </genre>

      <country>
        <xsl:call-template name="country">
          <xsl:with-param name="country" select="//ul[@class='filminfo-fields']/li[contains(text(), 'Quốc gia')]"/>
        </xsl:call-template>
      </country>

      <duration>
        <xsl:call-template name="duration">
          <xsl:with-param name="duration" select="//ul[@class='filminfo-fields']/li[contains(text(), 'Thời lượng')]"/>
        </xsl:call-template>
      </duration>

      <xsl:call-template name="director">
        <xsl:with-param name="director" select="//ul[@class='filminfo-fields']/li[contains(text(), 'Đạo diễn')]"/>
      </xsl:call-template>

      <xsl:call-template name="rating">
        <xsl:with-param name="rating" select="//ul[@class='filminfo-fields']/li[last()]"/>
      </xsl:call-template>

      <plot>
        <xsl:value-of select="//div[@itemprop='description']"/>
      </plot>

      <stars>
        <xsl:call-template name="collect-stars">
          <xsl:with-param name="stars" select="//div[@class='group-filminfo']/div" />
        </xsl:call-template>
      </stars>

      <image>
        <xsl:value-of select="//img[@itemprop='image']/@src" />
      </image>

      <url />
    </movie>
  </xsl:template>

  <xsl:template name="title">
    <xsl:param name="title" />
    <xsl:value-of select="$title//strong"/>
  </xsl:template>

  <xsl:template name="year">
    <xsl:param name="year" />
    <xsl:value-of select="$year/a"/>
  </xsl:template>

  <xsl:template name="genre">
    <xsl:param name="genre" />
    <xsl:value-of select="$genre/strong"/>
  </xsl:template>

  <xsl:template name="country">
    <xsl:param name="country" />
    <xsl:value-of select="$country//a"/>
  </xsl:template>

  <xsl:template name="duration">
    <xsl:param name="duration"/>
    <xsl:variable name="durationWithUnit" select="substring-after($duration/text(), ': ')"/>
    <xsl:value-of select="substring-before($durationWithUnit, ' ')"/>
  </xsl:template>

  <xsl:template name="director">
    <xsl:param name="director" />
    <xsl:choose>
      <xsl:when test="normalize-space($director)">
        <director>
          <xsl:value-of select="$director//a"/>
        </director>
      </xsl:when>
      <xsl:otherwise>
        <director>N/A</director>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="rating">
    <xsl:param name="rating" />
    <xsl:choose>
      <xsl:when test="matches($rating, '^\d+(\.\d{1,2})?$')">
        <rating>
          <xsl:value-of select="normalize-space($rating)"/>
        </rating>
      </xsl:when>
      <xsl:otherwise>
        <rating xsi:nil="true" />
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="collect-stars">
    <xsl:param name="stars" />
    <xsl:value-of select="string-join($stars//li//span/a, ', ')"/>
  </xsl:template>
</xsl:stylesheet>