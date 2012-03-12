figure(1);
X = [ 8, 10, 12, 14 ];
% Original Run - 32 bit
%SSPOPT = [ 1.02e+09, 9.90e+08, 1.44e+09, 2.69e+09, 2.64e+09 ];
%SSPLPR = [ 1.02e+09, 9.30e+08, 1.37e+09, 2.60e+09, 2.51e+09 ];
%SSPMST = [ 6.15e+08, 7.35e+08, 1.02e+09, 1.21e+09, 1.56e+09 ];
%SSPkNN = [ 9.80e+08, 8.50e+08, 1.27e+09, 2.07e+09, 2.32e+09 ];
% Rerun with MST+

SSPOPT = [ 8.32E+08, 1.08E+09, 1.54E+09, 2.28E+09 ];
SSPLPR = [ 8.32E+08, 1.08E+09, 1.54E+09, 2.21E+09 ];
SSPMST = [ 5.62E+08, 7.39E+08, 1.01E+09, 1.29E+09 ];
SSPkNN = [ 7.94E+08, 1.02E+09, 1.44E+09, 1.96E+09 ];
SSPMSTP = [ 8.36E+08, 1.08E+09, 1.55E+09, 2.21E+09 ];

SSPOPT = SSPOPT / 1000000;
SSPLPR = SSPLPR / 1000000;
SSPMST = SSPMST / 1000000;
SSPkNN = SSPkNN / 1000000;
SSPMSTP = SSPMSTP / 1000000;

plot(X,SSPOPT,'-+r','LineWidth',3);
hold on;
plot(X,SSPLPR,':*b','LineWidth',3);
plot(X,SSPMST,'-.sc','LineWidth',3);
plot(X,SSPkNN,'--og ','LineWidth',3);
plot(X,SSPMSTP, ':xm', 'LineWidth', 3);
                  
xlabel('Number of Nodes');
ylabel('Total Capacity (Mbps)');
legend('SSP-OPT','SSP-LPR','SSP-MST','SSP-kNN', 'SSP-MSTPlus', "Location", "NorthWest")
set(gca, 'XTickMode', 'manual', 'XTick', X);
hold off;
fixAxes;
print(1, "scenario1.eps", "-deps");
print(1, "scenario1.pdf", "-dpdf");
