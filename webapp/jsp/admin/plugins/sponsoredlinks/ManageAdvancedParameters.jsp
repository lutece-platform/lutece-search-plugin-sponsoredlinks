<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<link rel="stylesheet" type="text/css" href="css/plugins/sponsoredlinks/sponsoredlinks_admin.css" />

<jsp:useBean id="sponsoredlinks" scope="session" class="fr.paris.lutece.plugins.sponsoredlinks.web.SponsoredLinksJspBean" />

<% sponsoredlinks.init( request, sponsoredlinks.RIGHT_MANAGE_SPONSOREDLINKS ); %>
<%= sponsoredlinks.getManageAdvancedParameters( request ) %>

<%@ include file="../../AdminFooter.jsp" %>