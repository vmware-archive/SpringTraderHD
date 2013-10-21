<div class="span6">
    <!-- toggle -->
    <div id="toggle-busystock-control" class="show-busystock hide">
        <a class="accordion-toggle"><%= translate("busyStocks") %></a>
        <span class="border-bg"></span>
    </div>
    <!-- toggle -->
    <div id="busystock-control" class="well show-well">
        <div class="title"><h3><%= translate("Busy Stocks") %></h3></div>
        <table id="list-of-busystock" class="table table-striped table-bordered table-condensed">
            <thead>
                <tr>
                    <th><%= translate("Quote Symbol") %></th>
                    <th><%= translate("Count") %></th>
                </tr>
            </thead>
            <tbody></tbody>
        </table>
        <div id="no-busystock"></div>
    </div>
    <div class="pagination-container"/>
</div>