package com.mygdx.scngame.dialog;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import java.util.HashSet;

public class XmlDialogLoader extends SynchronousAssetLoader<DialogFile, XmlDialogLoader.DialogFileParameters> {

    public static final XmlReader xml = new XmlReader();
    public XmlDialogLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    public XmlDialogLoader() {
        super(new InternalFileHandleResolver());
    }

    @Override
    public DialogFile load(AssetManager assetManager, String fileName,
                           FileHandle file, DialogFileParameters parameter) {

        DialogFile dFile = new DialogFile();

        XmlReader.Element root = xml.parse(file);

        for(XmlReader.Element group : root.getChildrenByName("group")) {
            String groupName = group.getAttribute("id", "");

            for(XmlReader.Element dialog : group.getChildrenByName("dialog")) {
                DialogNode dialogNode = new DialogNode();
                dialogNode.id = dialog.getAttribute("id");

                for(XmlReader.Element message : dialog.getChildrenByName("message")) {
                    DialogMessage dialogMessage = new DialogMessage();
                    dialogMessage.speaker = message.getAttribute("speaker", "");
                    dialogMessage.message = message.getText();
                    dialogMessage.icon = assetManager.get(message.getAttribute("icon"), Texture.class);

                    String soundFile = "sfx/blipc5.mp3";

                    if(message.hasAttribute("sound")) {
                        soundFile = message.getAttribute("sound");
                    }

                    if(message.hasAttribute("pitch")) {
                        try {
                            dialogMessage.pitch = Float.parseFloat(message.getAttribute("pitch"));
                        } catch (NumberFormatException e) {
                            throw new NumberFormatException(e.getMessage() +
                                                           "\t pitch value of dialog file should be float");
                        }
                    }

                    dialogMessage.sound = assetManager.get(soundFile, Sound.class);

                    dialogNode.messages.add(dialogMessage);
                }
                dFile.addDialogNode(groupName, dialogNode);
            }
        }

        return dFile;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, DialogFileParameters parameter) {
        XmlReader.Element root = xml.parse(file);

        HashSet<String> textureDependencies = new HashSet<>();
        HashSet<String> soundDependencies = new HashSet<>();

        soundDependencies.add("sfx/blipc5.mp3");

        Array< XmlReader.Element> messages = root.getChildrenByNameRecursively("message");
        for(XmlReader.Element message : messages) {
            textureDependencies.add(message.getAttribute("icon"));

            String soundFile = null;

            if(message.hasAttribute("sound")) {
                soundFile = message.getAttribute("sound");
            }

            if(soundFile != null && !soundFile.isEmpty()) {
                soundDependencies.add(soundFile);
            }
        }

        Array<AssetDescriptor> dependencies = new Array<>();
        for(String dependencyFile : textureDependencies) {
            dependencies.add(new AssetDescriptor<>(dependencyFile, Texture.class));
        }

        for(String dependencyFile : soundDependencies) {
            dependencies.add(new AssetDescriptor<>(dependencyFile, Sound.class));
        }

        return dependencies;
    }

    public static class DialogFileParameters extends AssetLoaderParameters<DialogFile> {

    }
}
