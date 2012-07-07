using UnityEngine;

public class AssetHolder : ScriptableObject
{
    public Material GizmoMaterial;
    public Material AgentMaterial;
    public GameObject Knight;
	public GameObject Mage;
	public GameObject Archer;
	public GameObject Berzerker;

    private static AssetHolder _instance;
    public static AssetHolder Instance { get {return _instance; } }

    private AssetHolder()
    {
        _instance = this;
    }
}
