figure(3);
X = [ 4, 6, 8, 10, 12 ];
SSPLPR = [ 3.06e+09, 3.45e+09, 3.97e+09, 4.01e+09, 4.33e+09 ];
SSPMST = [ 2.18e+09, 2.17e+09, 2.11e+09  2.03e+09, 3.01e+09 ];
SSPkNN = [ 2.87e+09, 3.11e+09, 3.34e+09, 3.18e+09, 3.35e+09 ];
SSPLPR = SSPLPR / 1000000;
SSPMST = SSPMST / 1000000;
SSPkNN = SSPkNN / 1000000;
plot(X,SSPLPR,':*b','LineWidth',3);
hold on;
plot(X,SSPMST,'-..c','LineWidth',3);
plot(X,SSPkNN,'--og ','LineWidth',3);
xlabel('Number of Sectors');
ylabel('Throughput (Mbps)');
legend('SSP-LPR','SSP-MST','SSP-kNN', "Location", "NorthWest");
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
newpos = [0.15 0.15 0.8 0.8];        %// default is [0.13 0.11 0.775 0.815]
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
