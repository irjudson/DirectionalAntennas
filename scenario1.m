figure(1);
X = [ 8, 10, 12, 14 ];
SSPOPT = [ 1.02e+09, 9.90e+08, 1.44e+09, 2.69e+09 ];
SSPLPR = [ 1.02e+09, 9.30e+08, 1.37e+09, 2.60e+09 ];
SSPMST = [ 6.15e+08, 7.35e+08, 1.02e+09, 1.21e+09 ];
SSPkNN = [ 9.80e+08, 8.50e+08, 1.27e+09, 2.07e+09 ];
SSPOPT = SSPOPT / 1000000;
SSPLPR = SSPLPR / 1000000;
SSPMST = SSPMST / 1000000;
SSPkNN = SSPkNN / 1000000;
plot(X,SSPOPT,'--og ','LineWidth',3);
hold on;
plot(X,SSPLPR,':*b','LineWidth',3);
plot(X,SSPMST,'-..c','LineWidth',3);
plot(X,SSPkNN,'-+r','LineWidth',3);
xlabel('Number of Nodes');
ylabel('Throughput (Mbps)');
legend('SSP-OPT','SSP-LPR','SSP-MST','SSP-kNN', "Location", "NorthWest")
hold off;

function fixAxes
%---------------------------------------
%// Kludge to fix scaling of all figures
%// until GNU or I can find real fix.
%// Octave3.2.3 computes the scaling wrong
%// for this mac, such that the title 
%// and xlabel are not displayed.
%---------------------------------------
s = get(0,'showhiddenhandles');
set(0,'showhiddenhandles','on');
newpos = [0.15 0.15 0.78 0.78];        %// default is [0.13 0.11 0.775 0.815]
figs = get(0,'children');
if (~isempty(figs))
  for k=1:length(figs)
	  cax = get(figs(k),'currentaxes');
pos = get(cax,'position');       
if ~(pos(1) == newpos(1) && ... 
     pos(2) == newpos(2) && ...
     pos(3) == newpos(3) && ...
     pos(4) == newpos(4))
set(cax,'position',newpos);    
set(0,'currentfigure',figs(k));
drawnow();
        endif
    endfor
endif
	set(0,'showhiddenhandles',s);
%---------------------------------------
endfunction
%---------------------------------------
