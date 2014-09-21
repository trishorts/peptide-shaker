package eu.isas.peptideshaker.myparameters;

import com.compomics.util.experiment.biology.PTM;
import com.compomics.util.experiment.biology.PTMFactory;
import com.compomics.util.experiment.personalization.UrParameter;
import eu.isas.peptideshaker.scoring.PtmScoring;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

/**
 * This class contains the scores for the locations of the possible
 * modifications.
 *
 * @author Marc Vaudel
 */
public class PSPtmScores implements UrParameter {

    /**
     * Serial version UID for post-serialization compatibility.
     */
    static final long serialVersionUID = 7450340838299319636L;
    /**
     * A map containing all scores indexed by the modification of interest for a
     * peptide or a PSM.
     */
    private HashMap<String, PtmScoring> ptmMap = new HashMap<String, PtmScoring>();
    /**
     * A list of all modification sites confidently localized on a sequence in a
     * map: site -> PTM names.
     */
    private HashMap<Integer, ArrayList<String>> mainModificationSites = null;
    /**
     * A map of all confident modifications in a sequence indexed by ptm: PTM
     * name -> sites
     */
    private HashMap<String, ArrayList<Integer>> confidentModificationsByPTM = null;
    /**
     * A list of all secondary modifications in a sequence.
     *
     * @deprecated use the ambiguous modifications maps
     */
    private HashMap<Integer, ArrayList<String>> secondaryModificationSites = null;
    /**
     * A list of all ambiguous modifications in a sequence: representative site
     * -> secondary site -> PTM names
     */
    private HashMap<Integer, HashMap<Integer, ArrayList<String>>> ambiguousModificationsByRepresentativeSite = null;
    /**
     * Map of the representative sites a secondary site can map to: secondary
     * site -> representative sites
     */
    private HashMap<Integer, ArrayList<Integer>> secondaryToRepresentativesSitesMap = null;
    /**
     * A map of all ambiguous modifications in a sequence indexed by ptm: PTM
     * name -> representative site -> secondary sites
     */
    private HashMap<String, HashMap<Integer, ArrayList<Integer>>> ambiguousModificationsByPTM = null;

    /**
     * Constructor.
     */
    public PSPtmScores() {
    }

    /**
     * Adds a scoring result for the modification of interest.
     *
     * @param ptmName the modification of interest
     * @param ptmScoring the corresponding scoring
     */
    public void addPtmScoring(String ptmName, PtmScoring ptmScoring) {
        ptmMap.put(ptmName, ptmScoring);
    }

    /**
     * Returns the ptm scoring for the desired modification (null if none
     * found).
     *
     * @param ptmName the modification of interest
     * @return the scoring
     */
    public PtmScoring getPtmScoring(String ptmName) {
        return ptmMap.get(ptmName);
    }

    /**
     * Indicates whether a modification has been already scored.
     *
     * @param ptmName the modification of interest
     * @return a boolean indicating whether the modification is in the map
     */
    public boolean containsPtm(String ptmName) {
        return ptmMap.containsKey(ptmName);
    }

    /**
     * Returns a list of scored modifications.
     *
     * @return a list of scored modifications
     */
    public ArrayList<String> getScoredPTMs() {
        return new ArrayList<String>(ptmMap.keySet());
    }

    /**
     * Adds a confident modification site and removes the corresponding
     * ambiguous modification site if found.
     *
     * @param ptmName the modification name
     * @param modificationSite the modification site
     */
    public void addConfidentModificationSite(String ptmName, int modificationSite) {

        compatibilityCheck();
        
        // add the PTM to the site map
        if (mainModificationSites == null) {
            mainModificationSites = new HashMap<Integer, ArrayList<String>>();
        }
        ArrayList<String> ptms = mainModificationSites.get(modificationSite);
        if (ptms == null) {
            ptms = new ArrayList<String>();
            mainModificationSites.put(modificationSite, ptms);
        }
        if (!ptms.contains(ptmName)) {
            ptms.add(ptmName);
        }
        // add the site to the PTM map
        if (confidentModificationsByPTM == null) {
            confidentModificationsByPTM = new HashMap<String, ArrayList<Integer>>();
        }
        ArrayList<Integer> ptmSites = confidentModificationsByPTM.get(ptmName);
        if (ptmSites == null) {
            ptmSites = new ArrayList<Integer>();
            confidentModificationsByPTM.put(ptmName, ptmSites);
        }
        if (!ptmSites.contains(modificationSite)) {
            ptmSites.add(modificationSite);
        }
        // Check whether the site was already added as ambiguous and remove it if yes
        removeFromAmbiguousSitesMaps(ptmName, modificationSite);
    }

    /**
     * Removes a site from the ambiguous sites maps if found in there.
     *
     * @param ptmName the name of the PTM of interest
     * @param modificationSite the site of interest
     */
    private void removeFromAmbiguousSitesMaps(String ptmName, int modificationSite) {
            compatibilityCheck();
        if (ambiguousModificationsByPTM != null) {
            HashMap<Integer, ArrayList<Integer>> modificationSites = ambiguousModificationsByPTM.get(ptmName);
            if (modificationSites != null) {
                HashSet<Integer> representativeSites = new HashSet<Integer>(modificationSites.keySet());
                for (Integer representativeSite : representativeSites) {
                    ArrayList<Integer> secondarySites = modificationSites.get(representativeSite);
                    if (representativeSite == modificationSite || secondarySites.contains(modificationSite)) {
                        modificationSites.remove(representativeSite);
                        HashMap<Integer, ArrayList<String>> secondarySitesAtAa = ambiguousModificationsByRepresentativeSite.get(representativeSite);
                        HashSet<Integer> secondarySiteList = new HashSet<Integer>(secondarySitesAtAa.keySet());
                        for (Integer site : secondarySiteList) {
                            ArrayList<String> ptmList = secondarySitesAtAa.get(site);
                            ptmList.remove(ptmName);
                            if (ptmList.isEmpty()) {
                                secondarySitesAtAa.remove(site);
                                ArrayList<Integer> representativeSitesList = secondaryToRepresentativesSitesMap.get(site);
                                if (representativeSitesList != null) {
                                    representativeSitesList.remove(representativeSite);
                                    if (representativeSitesList.isEmpty()) {
                                        secondaryToRepresentativesSitesMap.remove(site);
                                    }
                                }
                            }
                        }
                        if (secondarySitesAtAa.isEmpty()) {
                            ambiguousModificationsByRepresentativeSite.remove(representativeSite);
                        }
                    }
                }
                if (modificationSites.isEmpty()) {
                    ambiguousModificationsByPTM.remove(ptmName);
                }
            }
        }
    }

    /**
     * Adds a group of modifications to the mapping of ambiguous sites if not
     * already listed as confident site.
     *
     * @param representativeSite the representative site of this modification
     * group
     * @param possibleModifications the possible modifications in a map: site ->
     * PTM name
     */
    public void addAmbiguousModificationSites(int representativeSite, HashMap<Integer, ArrayList<String>> possibleModifications) {
        if (ambiguousModificationsByRepresentativeSite == null) {
            ambiguousModificationsByRepresentativeSite = new HashMap<Integer, HashMap<Integer, ArrayList<String>>>();
            secondaryToRepresentativesSitesMap = new HashMap<Integer, ArrayList<Integer>>();
            ambiguousModificationsByPTM = new HashMap<String, HashMap<Integer, ArrayList<Integer>>>();
            compatibilityCheck();
        }
        HashMap<Integer, ArrayList<String>> modificationGroupsAtSite = ambiguousModificationsByRepresentativeSite.get(representativeSite);
        for (int site : possibleModifications.keySet()) {
            for (String ptmName : possibleModifications.get(site)) {
                if (!isConfidentModificationSite(site, ptmName)) {
                    if (modificationGroupsAtSite == null) {
                        modificationGroupsAtSite = new HashMap<Integer, ArrayList<String>>();
                        ambiguousModificationsByRepresentativeSite.put(representativeSite, modificationGroupsAtSite);
                    }
                    ArrayList<String> modifications = modificationGroupsAtSite.get(site);
                    if (modifications == null) {
                        modifications = new ArrayList<String>();
                        modificationGroupsAtSite.put(site, modifications);
                    }
                    if (!modifications.contains(ptmName)) {
                        modifications.add(ptmName);
                    }

                    if (site != representativeSite) {
                        ArrayList<Integer> representativeSites = secondaryToRepresentativesSitesMap.get(site);
                        if (representativeSites == null) {
                            representativeSites = new ArrayList<Integer>();
                            secondaryToRepresentativesSitesMap.put(site, representativeSites);
                        }
                        if (!representativeSites.contains(representativeSite)) {
                            representativeSites.add(representativeSite);
                        }
                    }

                    HashMap<Integer, ArrayList<Integer>> sites = ambiguousModificationsByPTM.get(ptmName);
                    if (sites == null) {
                        sites = new HashMap<Integer, ArrayList<Integer>>();
                        ambiguousModificationsByPTM.put(ptmName, sites);
                    }
                    ArrayList<Integer> secondarySites = sites.get(representativeSite);
                    if (secondarySites == null) {
                        secondarySites = new ArrayList<Integer>();
                        sites.put(representativeSite, secondarySites);
                    }
                    if (!secondarySites.contains(site)) {
                        secondarySites.add(site);
                    }
                }
            }
        }
    }

    /**
     * Changes the representative site for a given ambiguously localized PTM in
     * all maps.
     *
     * @param ptmName the name of the ptm of interest
     * @param originalRepresentativeSite the original representative site
     * @param newRepresentativeSite the new representative site
     */
    public void changeRepresentativeSite(String ptmName, Integer originalRepresentativeSite, Integer newRepresentativeSite) {
        HashMap<Integer, ArrayList<String>> ambiguousSites = ambiguousModificationsByRepresentativeSite.get(originalRepresentativeSite);
        if (ambiguousSites != null) {
            HashMap<Integer, ArrayList<String>> newSites = new HashMap<Integer, ArrayList<String>>();
            HashSet<Integer> sites = new HashSet<Integer>(ambiguousSites.keySet());
            for (Integer site : sites) {
                ArrayList<String> modifications = ambiguousSites.get(site);
                if (modifications.contains(ptmName)) {
                    ArrayList<String> newModifications = new ArrayList<String>();
                    newModifications.add(ptmName);
                    newSites.put(site, newModifications);
                    modifications.remove(ptmName);
                    if (modifications.isEmpty()) {
                        ambiguousSites.remove(site);
                    }
                    ArrayList<Integer> representativeSites = secondaryToRepresentativesSitesMap.get(site);
                    if (site == originalRepresentativeSite) {
                        if (representativeSites == null) {
                            representativeSites = new ArrayList<Integer>();
                            secondaryToRepresentativesSitesMap.put(site, representativeSites);
                        }
                        representativeSites.add(newRepresentativeSite);
                    } else if (site == newRepresentativeSite) {
                        representativeSites.remove(originalRepresentativeSite);
                        if (representativeSites.isEmpty()) {
                            secondaryToRepresentativesSitesMap.remove(site);
                        }
                    } else {
                        representativeSites.remove(originalRepresentativeSite);
                        representativeSites.add(newRepresentativeSite);
                    }
                }
            }
            if (ambiguousSites.isEmpty()) {
                ambiguousModificationsByRepresentativeSite.remove(originalRepresentativeSite);
            }
            ambiguousSites = ambiguousModificationsByRepresentativeSite.get(newRepresentativeSite);
            if (ambiguousSites == null) {
                ambiguousModificationsByRepresentativeSite.put(newRepresentativeSite, newSites);
            } else {
                for (int site : newSites.keySet()) {
                    ArrayList<String> modifications = ambiguousSites.get(site);
                    if (modifications == null) {
                        ambiguousSites.put(site, modifications);
                    } else {
                        modifications.add(ptmName);
                    }
                }
            }
        }
        HashMap<Integer, ArrayList<Integer>> ptmSiteMap = ambiguousModificationsByPTM.get(ptmName);
        ArrayList<Integer> secondarySites = ptmSiteMap.get(originalRepresentativeSite);
        if (secondarySites != null) {
            ptmSiteMap.remove(originalRepresentativeSite);
            ptmSiteMap.put(newRepresentativeSite, secondarySites);
        }
    }

    /**
     * Indicates whether a site is already registered as confident modification
     * site.
     *
     * @param site the site of interest
     * @param modificationName the name of the modification
     *
     * @return a boolean indicating whether a site is already registered as
     * confident modification site
     */
    public boolean isConfidentModificationSite(int site, String modificationName) {
        if (mainModificationSites == null) {
            return false;
        }
        ArrayList<String> modifications = mainModificationSites.get(site);
        if (modifications == null) {
            return false;
        }
        return modifications.contains(modificationName);
    }

    /**
     * Returns the main potential modifications at the given amino acid index.
     *
     * @param site the index in the sequence (0 is first amino acid) //@TODO:
     * check that it is 0 and not 1
     * @return a list containing all potential modifications as main match, an
     * empty list if none found
     */
    public ArrayList<String> getConfidentModificationsAt(int site) {
        ArrayList<String> result = null;
        if (mainModificationSites != null) {
            result = mainModificationSites.get(site);
        }
        if (result == null) {
            result = new ArrayList<String>();
        }
        return result;
    }

    /**
     * Returns the PTMs which have a representative ambiguous site at the given
     * site.
     *
     * @param site the index in the sequence (0 is first amino acid)
     *
     * @return a list of PTMs which have a representative ambiguous site at the
     * given site
     */
    public ArrayList<String> getPtmsAtRepresentativeSite(int site) {
        ArrayList<String> result = null;
        compatibilityCheck();
        if (ambiguousModificationsByRepresentativeSite != null) {
            HashMap<Integer, ArrayList<String>> ptmsAtSite = ambiguousModificationsByRepresentativeSite.get(site);
            if (ptmsAtSite != null) {
                result = ptmsAtSite.get(site);
            }
        }
        if (result == null) {
            result = new ArrayList<String>();
        }
        return result;
    }

    /**
     * Returns the confident sites for the given PTM. An empty list if none
     * found.
     *
     * @param PtmName the name of the PTM of interest
     *
     * @return the confident sites for the given PTM
     */
    public ArrayList<Integer> getConfidentSitesForPtm(String PtmName) {
        compatibilityCheck();
        ArrayList<Integer> confidentSites = null;
        if (confidentModificationsByPTM != null) {
            confidentSites = confidentModificationsByPTM.get(PtmName);
        }
        if (confidentSites == null) {
            confidentSites = new ArrayList<Integer>();
        }
        return confidentSites;
    }

    /**
     * Returns the ambiguous PTM assignments registered at the given
     * representative site in a map: secondary site -> PTMs.
     *
     * @param representativeSite the representative site of interest
     *
     * @return the ambiguous PTM assignments registered at the given
     * representative site
     */
    public HashMap<Integer, ArrayList<String>> getAmbiguousPtmsAtRepresentativeSite(int representativeSite) {
        compatibilityCheck();
        HashMap<Integer, ArrayList<String>> results = null;
        if (ambiguousModificationsByRepresentativeSite != null) {
            results = ambiguousModificationsByRepresentativeSite.get(representativeSite);
        }
        if (results == null) {
            results = new HashMap<Integer, ArrayList<String>>();
        }
        return results;
    }

    /**
     * Returns the ambiguous PTM assignments registered at the given site in a
     * map: representative site -> secondary site -> PTMs.
     *
     * @param site the site of interest
     *
     * @return the ambiguous PTM assignments registered at the given
     * representative site
     */
    public HashMap<Integer, HashMap<Integer, ArrayList<String>>> getAmbiguousPtmsAtSite(int site) {
        compatibilityCheck();
        HashMap<Integer, HashMap<Integer, ArrayList<String>>> results = null;
        if (ambiguousModificationsByRepresentativeSite != null) {
            HashMap<Integer, ArrayList<String>> resultsAtSite = ambiguousModificationsByRepresentativeSite.get(site);
            if (resultsAtSite != null) {
                results.put(site, resultsAtSite);
            }
            ArrayList<Integer> representativeSites = secondaryToRepresentativesSitesMap.get(site);
            if (representativeSites != null) {
                for (int representativeSite : representativeSites) {
                    resultsAtSite = ambiguousModificationsByRepresentativeSite.get(representativeSite);
                    if (resultsAtSite != null) {
                        results.put(representativeSite, resultsAtSite);
                    }
                }
            }
        }
        if (results == null) {
            results = new HashMap<Integer, HashMap<Integer, ArrayList<String>>>();
        }
        return results;
    }

    /**
     * Returns the ambiguous modification sites registered for the given PTM.
     *
     * @param ptmName the name of the PTM of interest
     *
     * @return the ambiguous modification sites registered for the given PTM
     */
    public HashMap<Integer, ArrayList<Integer>> getAmbiguousModificationsSites(String ptmName) {
        compatibilityCheck();
        HashMap<Integer, ArrayList<Integer>> results = null;
        if (ambiguousModificationsByPTM != null) {
            results = ambiguousModificationsByPTM.get(ptmName);
        }
        if (results == null) {
            results = new HashMap<Integer, ArrayList<Integer>>();
        }
        return results;
    }

    /**
     * Returns a list of all confident modification sites.
     *
     * @return a list of all confident modification sites
     */
    public ArrayList<Integer> getConfidentSites() {
        ArrayList<Integer> result = null;
        if (mainModificationSites != null) {
            result = new ArrayList<Integer>(mainModificationSites.keySet());
        }
        if (result == null) {
            result = new ArrayList<Integer>();
        }
        return result;
    }

    /**
     * Returns a list of all representative sites of ambiguously localized PTMs.
     *
     * @return a list of all representative sites of ambiguously localized PTMs
     */
    public ArrayList<Integer> getRepresentativeSites() {
        compatibilityCheck();
        ArrayList<Integer> result = null;
        if (ambiguousModificationsByRepresentativeSite != null) {
            result = new ArrayList<Integer>(ambiguousModificationsByRepresentativeSite.keySet());
        }
        if (result == null) {
            result = new ArrayList<Integer>();
        }
        return result;
    }

    /**
     * Returns a list of all sites of ambiguously localized PTMs.
     *
     * @return a list of all sites of ambiguously localized PTMs
     */
    public ArrayList<Integer> getAmbiguousSites() {
        compatibilityCheck();
        ArrayList<Integer> result = getRepresentativeSites();
        if (secondaryToRepresentativesSitesMap != null) {
            result.addAll(secondaryToRepresentativesSitesMap.keySet());
        }
        return result;
    }

    /**
     * Returns a list of ptms presenting at least a confident site.
     *
     * @return a list of ptms presenting at least a confident site
     */
    public ArrayList<String> getConfidentlyLocalizedPtms() {
        compatibilityCheck();
        ArrayList<String> result = null;
        if (confidentModificationsByPTM != null) {
            result = new ArrayList<String>(confidentModificationsByPTM.keySet());
        }
        if (result == null) {
            result = new ArrayList<String>();
        }
        return result;
    }

    /**
     * Returns a list of ptms presenting at least an ambiguous site.
     *
     * @return a list of ptms presenting at least an ambiguous site
     */
    public ArrayList<String> getAmbiguouslyLocalizedPtms() {
        compatibilityCheck();
        ArrayList<String> result = null;
        if (ambiguousModificationsByPTM != null) {
            result = new ArrayList<String>(ambiguousModificationsByPTM.keySet());
        }
        if (result == null) {
            result = new ArrayList<String>();
        }
        return result;
    }

    /**
     * For projects where the ambiguous modification sites map is not set, the
     * map is filled based on the secondary modification sites map. Secondary
     * sites are grouped when separated by less than 10 amino acids.
     */
    public void compatibilityCheck() {
        if (ambiguousModificationsByPTM == null && secondaryModificationSites != null) {
            PTMFactory ptmFactory = PTMFactory.getInstance();
            ArrayList<Integer> sites = new ArrayList<Integer>(secondaryModificationSites.keySet());
            Collections.sort(sites);
            HashMap<Double, Integer> lastSites = new HashMap<Double, Integer>();
            HashMap<Double, HashMap<Integer, ArrayList<String>>> currentAmbiguityGroups = new HashMap<Double, HashMap<Integer, ArrayList<String>>>();
            for (int site : sites) {
                for (String ptmName : secondaryModificationSites.get(site)) {
                    PTM ptm = ptmFactory.getPTM(ptmName);
                    double ptmMass = ptm.getMass();
                    Integer lastSite = lastSites.get(ptmMass);
                    if (lastSite != null && lastSite < site - 10) {
                        HashMap<Integer, ArrayList<String>> ambiguityGroups = currentAmbiguityGroups.get(ptmMass);
                        if (ambiguityGroups == null) {
                            ambiguityGroups = new HashMap<Integer, ArrayList<String>>();
                            currentAmbiguityGroups.put(ptmMass, ambiguityGroups);
                        }
                        ArrayList<String> modifications = ambiguityGroups.get(site);
                        if (modifications == null) {
                            modifications = new ArrayList<String>();
                            ambiguityGroups.put(site, modifications);
                        }
                        modifications.add(ptmName);
                        addAmbiguousModificationSites(lastSite, ambiguityGroups);
                        currentAmbiguityGroups.remove(ptmMass);
                    } else {
                        lastSites.put(ptmMass, site);
                        HashMap<Integer, ArrayList<String>> ambiguityGroups = currentAmbiguityGroups.get(ptmMass);
                        if (ambiguityGroups == null) {
                            ambiguityGroups = new HashMap<Integer, ArrayList<String>>();
                            currentAmbiguityGroups.put(ptmMass, ambiguityGroups);
                        }
                        ArrayList<String> modifications = ambiguityGroups.get(site);
                        if (modifications == null) {
                            modifications = new ArrayList<String>();
                            ambiguityGroups.put(site, modifications);
                        }
                        modifications.add(ptmName);
                    }
                }
            }
            for (double ptmMass : currentAmbiguityGroups.keySet()) {
                Integer lastSite = lastSites.get(ptmMass);
                HashMap<Integer, ArrayList<String>> ambiguityGroups = currentAmbiguityGroups.get(ptmMass);
                addAmbiguousModificationSites(lastSite, ambiguityGroups);
            }
            secondaryModificationSites = null;
        }
        if (confidentModificationsByPTM == null && mainModificationSites != null) {
            confidentModificationsByPTM = new HashMap<String, ArrayList<Integer>>();
            for (int site : mainModificationSites.keySet()) {
                for (String ptmName : mainModificationSites.get(site)) {
                    ArrayList<Integer> sites = confidentModificationsByPTM.get(ptmName);
                    if (sites == null) {
                        sites = new ArrayList<Integer>();
                        confidentModificationsByPTM.put(ptmName, sites);
                    }
                    sites.add(site);
                }
        }
        }
    }

    @Override
    public String getFamilyName() {
        return "PeptideShaker";
    }

    @Override
    public int getIndex() {
        return 3;
    }
}
