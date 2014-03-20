<#include "dashboard_header1.html">

<script src='${webcontext}/userContent/cucumber/jquery-1.11.0.min.js' type='text/javascript'></script>
<link rel='stylesheet' href='${webcontext}/userContent/cucumber/jquery.fancybox.css' type='text/css' media='screen'/>
<script type='text/javascript' src='${webcontext}/userContent/cucumber/jquery.fancybox.pack.js'></script>

<#include "dashboard_header2.html">

<div id='wrapper'>
    <div id='divmap'>
        <table id='tablemap'>
            <tr>
                <th>Grupo</th>
                <th class='colours'></th>
            </tr>
        <#list groups as group>
            <tr class='group ${group.name}'>
                <td>${group.name}</td> 
                <td class='colours' style='background-color:hsla(${group.hsl}, 0.75 );'></td>
            </tr>
        </#list>
        </table>
    </div>
    <div id='divresults'>
        <table id='results'>
            <tr>
                <th/>
                <th/>
            <#list browsers as browser>
                <th>${browser.name}<span class='browser'>${browser.version}</span>
                </th>
            </#list>    
            </tr>         
        <#list features as feature>
            <#assign groupsClasses>
               <#list feature.groups as group>
                  ${group.name}
               </#list>
            </#assign>  
            <tr class='results ${groupsClasses}'>
                <td class='groups'>
                    <table class='tigth'>
                        <tr>
                        <#list feature.groups as group>
                            <td style='background-color:hsla( ${group.hsl}, 0.75 );'></td>                                                          
                        </#list>
                        </tr>
                    </table>                 
                 </td>
                 <td class='feature'>
                     <a class='fancybox fancybox.iframe' href='${feature.buildUrl}/testngreports/${feature.classPackage}/${feature.clazz}'> 
                 	 ${feature.name}
                 	 </a>
                 </td>  
             <#list feature.browserResults as result>                  
             <#if result.status == "-">
                 <td style='background-color:rgba(175, 162, 162, 0.39);'>${result.summary}</td>
             <#elseif result.status == "OK">
                 <td style='background-color:rgba(153, 198, 142, 0.39);'>${result.summary}</td>
             <#elseif result.status == "KO">
                 <td style='background-color:rgba(247, 13, 26, 0.39);'>${result.summary}</td>
             <#else>
                 <td style='background-color:rgba(248, 240, 49, 0.39);'>${result.summary}</td>
             </#if>
             </#list>
             </tr>          
             <tr class='detailedresults'>
                <td class='none'></td>
                <td class='nomargin' colspan='${browserCount}'>
                    <table class='details' >
                    <#list feature.scenarioResults as scenario>
                        <tr>
                            <td class='scenario' colspan='50'>${scenario.name}</td>
                        </tr>         
                        <#list scenario.dataResults as data>
                            <tr>
                                <td class='details'>${data.value}</td>
                            <#list data.results as result>
                                	<td>${result}</td>
                                </#list>                               
                            </tr>    
                        </#list>
                    </#list>
                    </table>
                </td>
            </tr>            
        </#list>            
        </table>
    </div>
</div>
</body>
