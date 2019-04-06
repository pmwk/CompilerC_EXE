package src;

import java.io.Serializable;
import java.util.ArrayList;

public class Settings implements Serializable {

    public final static String types_counter[] = {"str", "number"};

    public Settings() {
        catalogPath = "/home/kanumba/Рабочий\\ стол/";
        lastFocusFile = null;

        nameOriginal = "";
        originalCounter = new Counter(false, typeCounter.getValues()[0], "");
        isSaveOriginal = true;

        isSaveCopied = false;
        catalogCopiedPath = "/";
        nameCopied = "";
        copiedCounter = new Counter(false, typeCounter.getValues()[0], "");

        isDeleteOriginal = false;
        isDeleteCopied = false;

        isLaunch = false;
        isLaunchCopied = false;
    }

    private String catalogPath;
    private String lastFocusFile;

    private String nameOriginal;
    private Counter originalCounter;
    private boolean isSaveOriginal;

    private boolean isSaveCopied;
    private String catalogCopiedPath;
    private String nameCopied;
    private Counter copiedCounter;

    private boolean isDeleteOriginal;
    private boolean isDeleteCopied;

    private boolean isLaunch;
    private boolean isLaunchCopied;

    public boolean isDeleteOriginal() {
        return isDeleteOriginal;
    }

    public void setDeleteOriginal(boolean deleteOriginal) {
        isDeleteOriginal = deleteOriginal;
    }

    public boolean isDeleteCopied() {
        return isDeleteCopied;
    }

    public void setDeleteCopied(boolean deleteCopied) {
        isDeleteCopied = deleteCopied;
    }

    public String getNameCopied() {
        return nameCopied;
    }

    public void setNameCopied(String nameCopied) {
        this.nameCopied = nameCopied;
    }

    public String getCatalogPath() {
        return catalogPath;
    }

    public void setCatalogPath(String catalogPath) {
        this.catalogPath = catalogPath;
    }

    public String getLastFocusFile() {
        return lastFocusFile;
    }

    public void setLastFocusFile(String lastFocusFile) {
        this.lastFocusFile = lastFocusFile;
    }

    public String getNameOriginal() {
        return nameOriginal;
    }

    public void setNameOriginal(String nameOriginal) {
        this.nameOriginal = nameOriginal;
    }

    public Counter getOriginalCounter() {
        return originalCounter;
    }

    public void setOriginalCounter(Counter originalCounter) {
        this.originalCounter = originalCounter;
    }

    public boolean isSaveOriginal() {
        return isSaveOriginal;
    }

    public void setSaveOriginal(boolean saveOriginal) {
        isSaveOriginal = saveOriginal;
    }

    public boolean isSaveCopied() {
        return isSaveCopied;
    }

    public void setSaveCopied(boolean saveCopied) {
        isSaveCopied = saveCopied;
    }

    public String getCatalogCopiedPath() {
        return catalogCopiedPath;
    }

    public void setCatalogCopiedPath(String catalogCopiedPath) {
        this.catalogCopiedPath = catalogCopiedPath;
    }

    public Counter getCopiedCounter() {
        return copiedCounter;
    }

    public void setCopiedCounter(Counter copiedCounter) {
        this.copiedCounter = copiedCounter;
    }

    public boolean isLaunch() {
        return isLaunch;
    }

    public void setLaunch(boolean launch) {
        isLaunch = launch;
    }

    public boolean isLaunchCopied() {
        return isLaunchCopied;
    }

    public void setLaunchCopied(boolean launchCopied) {
        isLaunchCopied = launchCopied;
    }

    public static settingsBuilder builder() {
        settingsBuilder settingsBuilder = new settingsBuilder();
        return settingsBuilder;
    }

    static class settingsBuilder {
        private Settings settings;
        public settingsBuilder() {
            settings = new Settings();
        }

        public settingsBuilder setCatalogPath(String catalogPath) {
            settings.setCatalogPath(catalogPath);
            return this;
        }

        public settingsBuilder setLastFocusFile(String name) {
            settings.setLastFocusFile(name);
            return this;
        }

        public settingsBuilder setNameOriginal(String nameOriginal) {
            settings.setNameOriginal(nameOriginal);
            return this;
        }

        public settingsBuilder setOriginalCounter(boolean isExist, String type, String lastValue) {
            Counter counter = new Counter(isExist, type, lastValue);
            settings.setOriginalCounter(counter);
            return this;
        }

        public settingsBuilder setSaveOriginal(boolean b) {
            settings.setSaveOriginal(b);
            return this;
        }

        public settingsBuilder setSaveCopied(boolean b) {
            settings.setSaveCopied(b);
            return this;
        }

        public settingsBuilder setCatalogCopiedPath(String catalogCopiedPath) {
            settings.setCatalogCopiedPath(catalogCopiedPath);
            return this;
        }

        public settingsBuilder setCopiedCounter(boolean isExist, String type, String lastValue) {
            Counter counter = new Counter(isExist, type, lastValue);
            settings.setCopiedCounter(counter);
            return this;
        }

        public settingsBuilder setLaunch(boolean b) {
            settings.setLaunch(b);
            return this;
        }

        public settingsBuilder setLaunchCopied(boolean b) {
            settings.setLaunchCopied(b);
            return this;
        }

        public settingsBuilder setNameCopied(String nameCopied) {
            settings.setNameCopied(nameCopied);
            return this;
        }

        public settingsBuilder setDeleteOriginal (boolean b) {
            settings.setDeleteOriginal(b);
            return this;
        }

        public settingsBuilder setDeleteCopied(boolean b) {
            settings.setDeleteCopied(b);
            return this;
        }

        public Settings build() {
            return settings;
        }
    }

    static class Counter implements Serializable {
        boolean isExist;
        typeCounter type;
        String lastValue;

        public Counter(boolean isExist, String type_str, String lastValue) {
            this.isExist = isExist;
            type = typeCounter.valueOf(type_str);
            this.lastValue = lastValue;
        }

        public boolean isExist() {
            return isExist;
        }

        public void setExist(boolean exist) {
            isExist = exist;
        }

        public String getType() {
            return type.getValue();
        }

        public void setType(String type_str) {
            this.type = typeCounter.valueOf(type_str);
        }

        public String getLastValue() {
            return lastValue;
        }

        public void setLastValue(String lastValue) {
            this.lastValue = lastValue;
        }
    }

    /*public static String convertTypeCounterToStr(typeCounter type) {

    }*/

    /*public static typeCounter convertStrToTypeCounter(String type_str) {
        typeCounter type;
        switch (type_str) {
            case "number":
                type = typeCounter.Number;
                break;
            default:
                type = typeCounter.Text;
        }

        return type;
    }*/

    public ArrayList<String> getErrors() {
        ArrayList<String> list = new ArrayList<>();

        if (!Catalog.isExistFile(catalogPath)) {
            list.add("The selected catalog does not exist [" + catalogPath + "]");
        } else if (lastFocusFile == null) {
            list.add("No file chosen");
        } else if (!Catalog.isExistFile(catalogPath + "/" + lastFocusFile)) {
            list.add("The selected file does not exist [" + catalogPath + "/" + lastFocusFile + "]");
        } else if (isSaveCopied == false && isSaveOriginal == false) {
            list.add("The result will not be saved anywhere");
        } else {
            if (isSaveOriginal) {
                String name = nameOriginal;
                if (originalCounter.isExist) {
                    typeCounter type = typeCounter.valueOf(originalCounter.getType());
                    name += Catalog.getNext_counter(type, originalCounter.getLastValue());
                }
                if (name == null || name.length() == 0) {
                    list.add("no file name is specified");
                } else {
                    name += ".exe";
                    if (Catalog.isExistFile(catalogPath + "/" + name)) {
                        if (!isDeleteOriginal) {
                            list.add("The resulting file is already in the directory ["  + catalogPath + "/" + name + "]" );
                        }
                    }
                }

            }

            if (isSaveCopied) {
                if (!Catalog.isExistFile(catalogCopiedPath)) {
                    list.add("copied catalog does not exist [" + catalogCopiedPath + "]");
                } else {
                    String nameC = nameCopied;
                    if (copiedCounter.isExist) {
                        typeCounter type = typeCounter.valueOf(copiedCounter.getType());
                        nameC += Catalog.getNext_counter(type, copiedCounter.getLastValue());
                    }
                    if (nameC == null || nameC.length() == 0) {
                        list.add("no file name (copied) is specified");
                    } else {
                        nameC += ".exe";
                        if (Catalog.isExistFile(catalogCopiedPath + "/" + nameC)) {
                            if (!isDeleteCopied) {
                                list.add("The resulting file (copied) is already in the directory [" + catalogCopiedPath + "/" + nameC +"]");
                            }
                        }
                    }
                }
            }
        }

        return list;
    }

    public String getPathSource() {
        String path = catalogPath + "/" + lastFocusFile;
        return path;
    }

    public String getPathForOriginalExe() {
        StringBuilder pathSource = new StringBuilder();

        pathSource.append(catalogPath);
        pathSource.append("/" + nameOriginal);
        if(originalCounter.isExist()) {
            pathSource.append(Catalog.getValueCounter(originalCounter));
        }
        pathSource.append(".exe");

        return pathSource.toString();
    }

    public String getPathForCopiedExe() {
        StringBuilder pathSource = new StringBuilder();

        pathSource.append(catalogCopiedPath);
        pathSource.append("/" + nameCopied);
        if(copiedCounter.isExist()) {
            pathSource.append(Catalog.getValueCounter(copiedCounter));
        }
        pathSource.append(".exe");

        return pathSource.toString();
    }
}
