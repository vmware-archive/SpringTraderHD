nano.views.BusyStock = Backbone.View.extend({

    events : {
        'click #toggle-busystock-control a' : 'toggle'
    },

    initialize : function(options) {
        nano.containers.busystock = this.$el;
    },

    render: function(model, page, hash) {
        'use strict';
        var ordersContent,
            paginator,
            title,
            pageCount = Math.ceil(model.totalRecords / model.pageSize);

        if (page > pageCount) {
            page = pageCount;
        }
        paginator = new nano.views.Paginator({
            pageCount: pageCount,            
            page: page,
            hash: hash,
            interval: nano.utils.getPaginationInterval(page, pageCount),
            onPageChange: _.bind(function(page) {
                model.fetch({
                    data : { page : page},
                    success : _.bind(function (model, response) {
                        this.renderRows(model);
                    }, this),
                    error : nano.utils.onApiError
                });    
            }, this)
        });
        

        // If it hasn't been rendered yet, build the section and store the key dom objects
        this.$el.html(_.template(nano.utils.getTemplate(nano.conf.tpls.busystock))());        
        // Embed the paginator into the container
        this.$el.find('.pagination-container').html(paginator.render());
        this.$el.show();
        this.tbody = this.$('#list-of-busystock > tbody'); // tbody of the busystock list
        this.toggleControl = this.$('#toggle-busystock-control'); // Toggle Control
        this.busyStockControl = this.$('#busystock-control'); // Busystock Control
        this.paginationControl = this.$('.pagination-container');
        
        //Prepare the view for collapsing sections
        if (!nano.utils.isMobile()) {
            title = this.$('div.title');
            // Check the hash and enable or disable the toggle list functionality
            if (location.hash === nano.conf.hash.dashboard || hash === nano.conf.hash.dashboardWithPage) {
                this.toggleControl.show();
                title.hide();
            } else {
                this.toggleControl.hide();
                title.show();
            }
        }
        
        // Check the page count of orders
        if (pageCount > 0) {
            // Render the list of orders
            this.renderRows(model);
            //alert("busy stock " + model.length);
        } else {
            //Render a no orders message
            this.noBusyStock();
        }
    },
    

    renderRows: function(model) {
        var i = 0,
            length = model.length,
            rows = '';
        for ( i; i < length; ++i ) {
            rows += _.template(nano.utils.getTemplate(nano.conf.tpls.busystockRow))(_.extend(model.at(i).toJSON(), {i:i}));
        }
        this.tbody.html(rows);
    },
    
    toggle : function(event){
        this.toggleControl.toggleClass('active');
        this.busyStockControl.toggle();
        this.paginationControl.toggle();
    },
    
    noBusyStock : function() {
        var htmlId = this.$('#no-busystock');
        htmlId.html(_.template(nano.utils.getTemplate(nano.conf.tpls.warning))({msg:'noDataAvailable'}) );
    }
    
});



