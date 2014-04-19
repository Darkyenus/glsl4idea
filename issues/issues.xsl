<?xml version="1.0" encoding="ISO-8859-15"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" encoding="ISO-8859-15"/>

    <xsl:template match="/tasks">
        <html>
            <head>
                <title>
                    <xsl:value-of select="@name"/>
                </title>
                <link rel="stylesheet" type="text/css" href="stylesheet.css"/>
                <script type='text/javascript'>
                    <![CDATA[
                        function showhide(event, id){
                            stopBubble(event);

                            //hide/unhide block
                            if (document.getElementById){
                                obj = document.getElementById(id);
                                if (obj.style.display != "block"){
                                    obj.style.display = "block";
                                } else {
                                    obj.style.display = "none";
                                }
                            }
                        }

                        function stopBubble(event){
                            //stop bubbling of events
                            if (window.event) {
                                window.event.cancelBubble = true;
                            } else if (event) {
                                event.stopPropagation();
                            }
                        }
                    ]]>
                </script>
            </head>

            <body>
                <xsl:apply-templates select="task"/>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="task">
        <div class="task" onclick="showhide(event, '{generate-id(.)}');">
            <h1>
                <xsl:value-of select="@name"/>

                <xsl:variable name="num_completed" select="count(descendant::subtask[not(child::subtask) and @completed='true'])"/>
                <xsl:variable name="num_subtasks" select="count(descendant::subtask[not(child::subtask)])"/>
                <xsl:variable name="progress" select="($num_completed div $num_subtasks) * 100"/>

                <xsl:variable name="progress_color" select="round($progress div 5) * 5"/>

                <div class="progress-container">
                    <div class="pct{$progress_color}" style="width: {$progress}%"> </div>
                    <div class="progress-label"><xsl:value-of select="round($progress)"/>%</div>
                </div>
            </h1>

            <h2>
                <xsl:copy-of select="description"/>
            </h2>

            <div id="{generate-id(.)}" class="subtasklist">
                <xsl:apply-templates select="subtask"/>
            </div>
        </div>
    </xsl:template>

    <xsl:template match="subtask">
        <!-- Legga inn deadline med sjekking av dato og highlight dersom dette er i dag...-->

        <xsl:variable name="children" select="count(subtask)"/>
        <xsl:variable name="num_completed" select="count(descendant::subtask[not(child::subtask) and @completed='true'])"/>
        <xsl:variable name="num_subtasks" select="count(descendant::subtask[not(child::subtask)])"/>

        <xsl:variable name="done">
            <xsl:if test="@completed='true' or ($children > 0 and $num_completed=$num_subtasks)">done</xsl:if>
        </xsl:variable>

        <xsl:variable name="hoverable">
            <xsl:if test="count(info) > 0">hoverable</xsl:if>
        </xsl:variable>

        <!--<a href="{$children > 0}_{$num_completed}_{$num_subtasks}">link</a>-->

        <div class="subtaskgroup" onclick="stopBubble(event);">

            <div class="subtask {$hoverable}" onclick="showhide(event, '{generate-id(.)}');">
                <xsl:choose>
                    <xsl:when test="$done='done'">
                        <img class="left" src="accept.png"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <img class="left" src="remove.png"/>
                    </xsl:otherwise>
                </xsl:choose>


                <xsl:if test="count(info) > 0">
                    <img class="right" src="down.png"/>
                </xsl:if>
                <h1 class="{$done}">
                    <xsl:value-of select="@name"/>
                </h1>


                <!--<xsl:value-of select="generate-id(.)"/>-->
            </div>
            <div class="info" id="{generate-id(.)}" onclick="stopBubble(event);">

                <xsl:for-each select="info">
                    <xsl:variable name="alternate">
                        <xsl:if test="position() mod 2 = 0">darkinfo</xsl:if>
                    </xsl:variable>

                    <h2 class="{$alternate}">
                        <xsl:copy-of select="."/>
                    </h2>
                </xsl:for-each>
            </div>

            <xsl:apply-templates select="subtask"/>
        </div>
    </xsl:template>

</xsl:stylesheet>
