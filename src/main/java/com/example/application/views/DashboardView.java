package com.example.application.views;

import com.example.application.services.CrmService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Dashboard | Vaadin CRM")
public class DashboardView extends VerticalLayout {
  private final CrmService crmService;

  public DashboardView(CrmService crmService) {
    this.crmService = crmService;
    addClassName("dashboard-view");
    setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    add(getContactStats(), getCompaniesChart());
  }

  private Component getContactStats() {
    Span stats = new Span(crmService.countContacts() + " contacts");
    stats.addClassNames(
      LumoUtility.FontSize.XLARGE,
      LumoUtility.Margin.Top.MEDIUM
    );
    return stats;
  }

  private Chart getCompaniesChart() {
    Chart chart = new Chart(ChartType.PIE);

    DataSeries dataSeries = new DataSeries();
    crmService
      .findAllCompanies()
      .forEach(company -> dataSeries.add(new DataSeriesItem(company.getName(), company.getEmployeeCount())));

    chart.getConfiguration().setSeries(dataSeries);

    return chart;
  }
}
